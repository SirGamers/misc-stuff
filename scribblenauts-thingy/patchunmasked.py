def toggle_patch_byte(offset):
    with open("Scribble.exe", 'r+b') as f:
        f.seek(offset)
        current_byte = f.read(1)
        if not current_byte:
            print(f"Error: Reached EOF at offset 0x{offset:X}")
            return
        current_value = current_byte[0]
        if current_value == 0x41:
            f.seek(offset)
            f.write(bytes([0x21]))
            print(f"Patched byte at 0x{offset:X}: {0x41:02X} → {0x21:02X}")
        elif current_value == 0x21:
            f.seek(offset)
            f.write(bytes([0x41]))
            print(f"Unpatched byte at 0x{offset:X}: {0x21:02X} → {0x41:02X}")
        else:
            print(f"Unexpected value at 0x{offset:X}: {current_value:02X}. No changes made.")

toggle_patch_byte(0x1BE691)