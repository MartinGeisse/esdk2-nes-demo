
# Day 1

Created a project and repository. Added ESDK2 and LWJGL dependencies. 

The simulator GUI window opens, but drawing a test pixel doesn't work yet.

# Day 2

Added cartridge file loader.
Added stub for sequential CPU model.
Added stub for sequential PPU model.
Added temporary WIP versions of NES and 6502 docs.

Made the test pixel work. The problem was the inverted Y axis in OpenGL screen coordinates.

Added the NES system palette and managed to show the pattern table (from a CHR-ROM) on screen.

CPU uses reset vector and fetches first instruction bytes correctly.

# Day 3

Read the PPU specs over and over again to understand how it decodes its own address space. Wrote some
code in an attempt to simulate that.


 