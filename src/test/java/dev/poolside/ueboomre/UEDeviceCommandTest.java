package dev.poolside.ueboomre;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.logitech.ue.centurion.device.command.UEDeviceCommand;
import com.logitech.ue.centurion.device.devicedata.UESonificationProfile;
import com.logitech.ue.centurion.device.devicedata.UESoundProfile;
import java.util.StringJoiner;
import org.junit.jupiter.api.Test;

class UEDeviceCommandTest {

    private String convertByteToHexadecimal(byte[] byteArray) {
        var hex = new StringJoiner(" ");
        for (byte i : byteArray) {
            hex.add(String.format("%02X", i));
        }
        return hex.toString();
    }

    @Test
    void adjustVolumeUpByOne() {
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.AdjustVolume, new byte[] { 1, (byte) 1 });
        var expected = new byte[] { 4, 1, -69, 1, 1 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("04 01 BB 01 01", convertByteToHexadecimal(output));
    }

    @Test
    void announceBatteryLevel() {
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.AnnounceBatteryLevel, null);
        var expected = new byte[] { 2, 1, 107 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("02 01 6B", convertByteToHexadecimal(output));
    }

    @Test
    void setSonfication() {
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetSonfication,
                new byte[] { (byte) UESonificationProfile.getCode(UESonificationProfile.CONGA) });
        var expected = new byte[] { 3, 1, 101, 1 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("03 01 65 01", convertByteToHexadecimal(output));
    }

    @Test
    void setSonficationOff() {
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetSonfication,
                new byte[] { (byte) UESonificationProfile.getCode(UESonificationProfile.NONE) });
        var expected = new byte[] { 3, 1, 101, 0 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("03 01 65 00", convertByteToHexadecimal(output));
    }

    @Test
    void emitSound() {
        int code = UESoundProfile.POWER_ON.getCode();
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.PlaySound,
                new byte[] { (byte) (code >> 8 & 0xFF), (byte) (code & 0xFF) });
        var expected = new byte[] { 4, 1, 108, 96, -64 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("04 01 6C 60 C0", convertByteToHexadecimal(output));
    }

    @Test
    void setBLEStateToFalse() {
        boolean isEnabled = false;
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBTLEState,
                new byte[] { (byte) (isEnabled ? 1 : 0) });
        var expected = new byte[] { 3, 1, -71, 0 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("03 01 B9 00", convertByteToHexadecimal(output));
    }

    @Test
    void setBLEStateToTrue() {
        boolean isEnabled = true;
        var cmd = UEDeviceCommand.newCommand(UEDeviceCommand.UECommand.SetBTLEState,
                new byte[] { (byte) (isEnabled ? 1 : 0) });
        var expected = new byte[] { 3, 1, -71, 1 };
        var output = cmd.buildCommandData();
        assertArrayEquals(expected, output);
        assertEquals("03 01 B9 01", convertByteToHexadecimal(output));
    }
}