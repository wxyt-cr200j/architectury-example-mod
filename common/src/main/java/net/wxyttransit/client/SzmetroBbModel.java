package net.wxyttransit.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import mtr.render.MoreRenderLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SzmetroBbModel {

	private static final Map<ResourceLocation, SzmetroBbModel> CACHE = new ConcurrentHashMap<>();

	private final List<Element> elements;

	public SzmetroBbModel(List<Element> elements) {
		this.elements = elements;
	}

	public static SzmetroBbModel get(ResourceLocation modelId) {
		return CACHE.computeIfAbsent(modelId, SzmetroBbModel::load);
	}

	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, Map<String, ResourceLocation> texturesById) {
		for (final Element element : elements) {
			element.render(poseStack, bufferSource, light, texturesById);
		}
	}

	public void render(PoseStack poseStack, int light, VertexConsumer vertexConsumer) {
		for (final Element element : elements) {
			element.render(poseStack, light, vertexConsumer);
		}
	}

	private static SzmetroBbModel load(ResourceLocation modelId) {
		try {
			final Resource resource = Minecraft.getInstance().getResourceManager().getResource(modelId).orElseThrow();
			try (final InputStreamReader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
				final JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
				final Map<String, TextureMeta> textureMeta = new HashMap<>();
				for (final JsonElement textureElement : root.getAsJsonArray("textures")) {
					final JsonObject textureObject = textureElement.getAsJsonObject();
					textureMeta.put(
							textureObject.get("id").getAsString(),
							new TextureMeta(
									textureObject.has("uv_width") ? textureObject.get("uv_width").getAsFloat() : root.getAsJsonObject("resolution").get("width").getAsFloat(),
									textureObject.has("uv_height") ? textureObject.get("uv_height").getAsFloat() : root.getAsJsonObject("resolution").get("height").getAsFloat()
							)
					);
				}

				final List<Element> elements = new ArrayList<>();
				for (final JsonElement elementJson : root.getAsJsonArray("elements")) {
					final JsonObject elementObject = elementJson.getAsJsonObject();
					elements.add(parseElement(elementObject, textureMeta));
				}
				return new SzmetroBbModel(elements);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new SzmetroBbModel(List.of());
		}
	}

	private static Element parseElement(JsonObject elementObject, Map<String, TextureMeta> textureMeta) {
		final float[] from = readScaledVec3(elementObject.getAsJsonArray("from"));
		final float[] to = readScaledVec3(elementObject.getAsJsonArray("to"));
		final float[] origin = elementObject.has("origin") ? readScaledVec3(elementObject.getAsJsonArray("origin")) : new float[]{0, 0, 0};
		final float[] rotation = elementObject.has("rotation") ? readRawVec3(elementObject.getAsJsonArray("rotation")) : new float[]{0, 0, 0};

		final Map<Direction, Face> faces = new EnumMap<>(Direction.class);
		if (elementObject.has("faces")) {
			final JsonObject facesObject = elementObject.getAsJsonObject("faces");
			for (final Map.Entry<String, JsonElement> entry : facesObject.entrySet()) {
				final Direction direction = parseDirection(entry.getKey());
				if (direction == null) {
					continue;
				}

				final JsonObject faceObject = entry.getValue().getAsJsonObject();
				if (!faceObject.has("texture") || !faceObject.has("uv")) {
					continue;
				}

				final String textureId = faceObject.get("texture").getAsString().replace("#", "");
				final TextureMeta meta = textureMeta.get(textureId);
				if (meta == null) {
					continue;
				}

				final JsonArray uvArray = faceObject.getAsJsonArray("uv");
				faces.put(direction, new Face(
						textureId,
						uvArray.get(0).getAsFloat() / meta.uvWidth,
						uvArray.get(1).getAsFloat() / meta.uvHeight,
						uvArray.get(2).getAsFloat() / meta.uvWidth,
						uvArray.get(3).getAsFloat() / meta.uvHeight,
						faceObject.has("rotation") ? faceObject.get("rotation").getAsInt() : 0
				));
			}
		}

		return new Element(from, to, origin, rotation, faces);
	}

	private static float[] readScaledVec3(JsonArray array) {
		return new float[]{
				array.get(0).getAsFloat() / 16F,
				array.get(1).getAsFloat() / 16F,
				array.get(2).getAsFloat() / 16F
		};
	}

	private static float[] readRawVec3(JsonArray array) {
		return new float[]{
				array.get(0).getAsFloat(),
				array.get(1).getAsFloat(),
				array.get(2).getAsFloat()
		};
	}

	private static Direction parseDirection(String name) {
		switch (name) {
			case "north": return Direction.NORTH;
			case "south": return Direction.SOUTH;
			case "east":  return Direction.EAST;
			case "west":  return Direction.WEST;
			case "up":    return Direction.UP;
			case "down":  return Direction.DOWN;
			default:      return null;
		}
	}

	private static void emitVertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vertex vertex, float u, float v, Direction direction, int light) {
		vertexConsumer.vertex(pose.pose(), vertex.x, vertex.y, vertex.z)
				.color(255, 255, 255, 255)
				.uv(u, v)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.uv2(light)
				.normal(pose.normal(), direction.getStepX(), direction.getStepY(), direction.getStepZ())
				.endVertex();
	}

	private static final class TextureMeta {
		private final float uvWidth;
		private final float uvHeight;

		private TextureMeta(float uvWidth, float uvHeight) {
			this.uvWidth = uvWidth;
			this.uvHeight = uvHeight;
		}
	}

	private static final class Element {

		private static final float FACE_EPSILON = 1F / 1024F;

		private final float[] from;
		private final float[] to;
		private final float[] origin;
		private final float[] rotation;
		private final Map<Direction, Face> faces;

		private Element(float[] from, float[] to, float[] origin, float[] rotation, Map<Direction, Face> faces) {
			this.from = from;
			this.to = to;
			this.origin = origin;
			this.rotation = rotation;
			this.faces = faces;
		}

		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, Map<String, ResourceLocation> texturesById) {
			poseStack.pushPose();
			poseStack.translate(origin[0], origin[1], origin[2]);
			if (rotation[2] != 0) poseStack.mulPose(Axis.ZP.rotationDegrees(rotation[2]));
			if (rotation[1] != 0) poseStack.mulPose(Axis.YP.rotationDegrees(rotation[1]));
			if (rotation[0] != 0) poseStack.mulPose(Axis.XP.rotationDegrees(rotation[0]));
			poseStack.translate(-origin[0], -origin[1], -origin[2]);

			for (final Map.Entry<Direction, Face> entry : faces.entrySet()) {
				final ResourceLocation texture = texturesById.get(entry.getValue().textureId);
				if (texture == null) continue;

				final Vertex[] vertices = createVertices(entry.getKey());
				if (vertices == null) continue;

				final VertexConsumer vertexConsumer = bufferSource.getBuffer(MoreRenderLayers.getExterior(texture));
				final PoseStack.Pose pose = poseStack.last();
				final Face face = entry.getValue();
				emitVertices(vertexConsumer, pose, vertices, face, entry.getKey(), light);
			}

			poseStack.popPose();
		}

		private void emitVertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vertex[] vertices, Face face, Direction direction, int light) {
			emitVertex(vertexConsumer, pose, vertices[0], face.getU(0), face.getV(0), direction, light);
			emitVertex(vertexConsumer, pose, vertices[1], face.getU(1), face.getV(1), direction, light);
			emitVertex(vertexConsumer, pose, vertices[2], face.getU(2), face.getV(2), direction, light);
			emitVertex(vertexConsumer, pose, vertices[3], face.getU(3), face.getV(3), direction, light);
		}

		public void render(PoseStack poseStack, int light, VertexConsumer vertexConsumer) {
			poseStack.pushPose();
			poseStack.translate(origin[0], origin[1], origin[2]);
			if (rotation[2] != 0) poseStack.mulPose(Axis.ZP.rotationDegrees(rotation[2]));
			if (rotation[1] != 0) poseStack.mulPose(Axis.YP.rotationDegrees(rotation[1]));
			if (rotation[0] != 0) poseStack.mulPose(Axis.XP.rotationDegrees(rotation[0]));
			poseStack.translate(-origin[0], -origin[1], -origin[2]);

			for (final Map.Entry<Direction, Face> entry : faces.entrySet()) {
				final Vertex[] vertices = createVertices(entry.getKey());
				if (vertices == null) continue;

				final PoseStack.Pose pose = poseStack.last();
				final Face face = entry.getValue();
				emitVertices(vertexConsumer, pose, vertices, face, entry.getKey(), light);
			}

			poseStack.popPose();
		}

		private Vertex[] createVertices(Direction direction) {
			final float fx = from[0];
			final float fy = from[1];
			final float fz = from[2];
			final float tx = to[0];
			final float ty = to[1];
			final float tz = to[2];
			final boolean flatX = Math.abs(tx - fx) < 1E-6F;
			final boolean flatY = Math.abs(ty - fy) < 1E-6F;
			final boolean flatZ = Math.abs(tz - fz) < 1E-6F;
			final float northZ = flatZ ? fz - FACE_EPSILON : fz;
			final float southZ = flatZ ? tz + FACE_EPSILON : tz;
			final float westX = flatX ? fx - FACE_EPSILON : fx;
			final float eastX = flatX ? tx + FACE_EPSILON : tx;
			final float downY = flatY ? fy - FACE_EPSILON : fy;
			final float upY = flatY ? ty + FACE_EPSILON : ty;

			switch (direction) {
				case NORTH: return new Vertex[]{new Vertex(tx, ty, northZ), new Vertex(tx, fy, northZ), new Vertex(fx, fy, northZ), new Vertex(fx, ty, northZ)};
				case SOUTH: return new Vertex[]{new Vertex(fx, ty, southZ), new Vertex(fx, fy, southZ), new Vertex(tx, fy, southZ), new Vertex(tx, ty, southZ)};
				case WEST:  return new Vertex[]{new Vertex(westX, ty, fz), new Vertex(westX, fy, fz), new Vertex(westX, fy, tz), new Vertex(westX, ty, tz)};
				case EAST:  return new Vertex[]{new Vertex(eastX, ty, tz), new Vertex(eastX, fy, tz), new Vertex(eastX, fy, fz), new Vertex(eastX, ty, fz)};
				case UP:    return new Vertex[]{new Vertex(fx, upY, fz), new Vertex(fx, upY, tz), new Vertex(tx, upY, tz), new Vertex(tx, upY, fz)};
				case DOWN:  return new Vertex[]{new Vertex(fx, downY, tz), new Vertex(fx, downY, fz), new Vertex(tx, downY, fz), new Vertex(tx, downY, tz)};
				default:    return null;
			}
		}
	}

	private static final class Face {
		private final String textureId;
		private final float u1, v1, u2, v2;
		private final int rotation;

		private Face(String textureId, float u1, float v1, float u2, float v2, int rotation) {
			this.textureId = textureId;
			this.u1 = u1;
			this.v1 = v1;
			this.u2 = u2;
			this.v2 = v2;
			this.rotation = Math.floorMod(rotation, 360);
		}

		public float getU(int vertexIndex) {
			final int i = Math.floorMod(vertexIndex + rotation / 90, 4);
			return i == 0 || i == 1 ? u1 : u2;
		}

		public float getV(int vertexIndex) {
			final int i = Math.floorMod(vertexIndex + rotation / 90, 4);
			return i == 0 || i == 3 ? v1 : v2;
		}
	}

	private static final class Vertex {
		private final float x, y, z;
		private Vertex(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}