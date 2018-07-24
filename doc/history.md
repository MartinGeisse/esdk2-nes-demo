
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

# Day 4..7

Read CPU docs over and over again.

# Day 8

Start implementing a sequential CPU model.

# Day 9

Continued implementing CPU instructions; added PPU control / mask / status register stubs so my test ROM
gets some code executed without errors.

# Day 10

CPU instructions should be mostly finished but probably still contain bugs.
Worked on PPU background rendering, but it's not working yet.

# Day 11

Realized that a NES clone on an FPGA is fundamentally different from building something
new on an FPGA. Here I have existing software that I can't change and for which I can't
build a model -- I have to take it "as is".

This affects simulation performance. OTOH, I can recognize specific program sequences
and optimize for them. For example, when the program runs into an endless loop, I can
recognize the jump to itself and simulate passive sleep instead of active sleep (usually
at the end of a frame, when waiting for the next interrupt or NMI to happen).

Also skimmed the CPU code again to find bugs, and fixed some.

# Day 12

Fixed various bugs in the CPU and the PPU. Especially the PPU has some non-obvious quirks,
such as the shared low/high byte toggle when loading registers 0x2005 and 0x2006.

# Day 13

Lots of PPU model bug fixes. (Note that all this is still sequential simulation only -- no RTL yet).
The "Hello world" demo finally runs correctly.

Also, I added a stub for the APU and implemented controller 1 input. Seems to work.
