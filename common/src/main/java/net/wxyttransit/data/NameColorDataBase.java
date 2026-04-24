package net.wxyttransit.data;

import io.netty.buffer.Unpooled;
import mtr.data.EnumHelper;
import mtr.data.MessagePackHelper;
import mtr.data.SerializedDataBase;
import mtr.data.TransportMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public abstract class NameColorDataBase extends SerializedDataBase implements Comparable<NameColorDataBase> {

	public final String id;
	public final TransportMode transportMode;
	public String name;
	public int color;

	private static final String KEY_ID = "id";
	private static final String KEY_TRANSPORT_MODE = "transport_mode";
	private static final String KEY_NAME = "name";
	private static final String KEY_COLOR = "color";

	public NameColorDataBase() {
		this("");
	}

	public NameColorDataBase(String id) {
		this(id, TransportMode.TRAIN);
	}

	public NameColorDataBase(TransportMode transportMode) {
		this("", transportMode);
	}

	public NameColorDataBase(String id, TransportMode transportMode) {
		this.id = id;
		this.transportMode = transportMode;
		name = "";
	}

	public NameColorDataBase(Map<String, Value> map) {
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		id = messagePackHelper.getString(KEY_ID);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, messagePackHelper.getString(KEY_TRANSPORT_MODE));
		name = messagePackHelper.getString(KEY_NAME);
		color = messagePackHelper.getInt(KEY_COLOR);
	}

	@Deprecated
	public NameColorDataBase(CompoundTag compoundTag) {
		id = compoundTag.getString(KEY_ID);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, compoundTag.getString(KEY_TRANSPORT_MODE));
		name = compoundTag.getString(KEY_NAME);
		color = compoundTag.getInt(KEY_COLOR);
	}

	public NameColorDataBase(FriendlyByteBuf packet) {
		id = packet.readUtf();
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packet.readUtf(PACKET_STRING_READ_LENGTH));
		name = packet.readUtf(PACKET_STRING_READ_LENGTH).replace(" |", "|").replace("| ", "|");
		color = packet.readInt();
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		messagePacker.packString(KEY_ID).packString(id);
		messagePacker.packString(KEY_TRANSPORT_MODE).packString(transportMode.toString());
		messagePacker.packString(KEY_NAME).packString(name);
		messagePacker.packString(KEY_COLOR).packInt(color);
	}

	@Override
	public int messagePackLength() {
		return 4;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		packet.writeUtf(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(name);
		packet.writeInt(color);
	}

	public void update(String key, FriendlyByteBuf packet) {
		if (key.equals(KEY_NAME)) {
			name = packet.readUtf(PACKET_STRING_READ_LENGTH);
			color = packet.readInt();
		}
	}

	public void setNameColor(Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_NAME);
		packet.writeUtf(name);
		packet.writeInt(color);
		if (sendPacket != null) {
			sendPacket.accept(packet);
		}
	}

	public final boolean isTransportMode(TransportMode transportMode) {
		return !hasTransportMode() || this.transportMode == transportMode;
	}

	protected abstract boolean hasTransportMode();

	@Override
	public int compareTo(NameColorDataBase compare) {
		return (name.toLowerCase(Locale.ENGLISH) + color).compareTo((compare.name + compare.color).toLowerCase(Locale.ENGLISH));
	}
}
