/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author db1992
 */

package interstellar.system.mos6502.constants;

public final class Tables
{
    // Recheck the whole instruction table ^^
    public static enum AddressingMode
    {
        Accumulator,
        Absolute,
        AbsoluteX,
        AbsoluteY,
        Indirect,
        IndirectX,
        IndirectY,
        ZeroPage,
        ZeroPageX,
        ZeroPageY,
        Immediate,
        Implied,
        Relative,
        NotAvailable;
    }

    public static final AddressingMode[] OPADDRESSING =
    {
    //             0x?0                    0x?1                      0x?2                        0x?3                     0x?4                     0x?5                     0x?6                     0x?7                     0x?8                   0x?9                     0x?A                       0x?B                     0x?C                     0x?D                     9x?E                     0x?F
        AddressingMode.Implied,  AddressingMode.IndirectX,  AddressingMode.NotAvailable, AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Accumulator, AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0x0?
        AddressingMode.Relative, AddressingMode.IndirectY,  AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0x1?
        AddressingMode.Absolute, AddressingMode.IndirectX,  AddressingMode.NotAvailable, AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Accumulator, AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0x2?
        AddressingMode.Relative, AddressingMode.IndirectY,  AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0x3?

        AddressingMode.Implied,  AddressingMode.IndirectX,  AddressingMode.NotAvailable, AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Accumulator, AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0x4?
        AddressingMode.Relative, AddressingMode.IndirectY,  AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0x5?
        AddressingMode.Implied,  AddressingMode.IndirectX,  AddressingMode.NotAvailable, AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Accumulator, AddressingMode.Immediate, AddressingMode.Indirect,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0x6?
        AddressingMode.Relative, AddressingMode.IndirectY,  AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0x7?

        AddressingMode.Immediate, AddressingMode.IndirectX, AddressingMode.Immediate,    AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Implied,     AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0x8?
        AddressingMode.Relative,  AddressingMode.IndirectY, AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageY, AddressingMode.ZeroPageY, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteY, AddressingMode.AbsoluteY, // 0x9?
        AddressingMode.Immediate, AddressingMode.IndirectX, AddressingMode.Immediate,    AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Implied,     AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0xA?
        AddressingMode.Relative,  AddressingMode.IndirectY, AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageY, AddressingMode.ZeroPageY, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteY, AddressingMode.AbsoluteY, // 0xB?

        AddressingMode.Immediate, AddressingMode.IndirectX, AddressingMode.Immediate,    AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Implied,     AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0xC?
        AddressingMode.Relative,  AddressingMode.IndirectY, AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0xD?
        AddressingMode.Immediate, AddressingMode.IndirectX, AddressingMode.Immediate,    AddressingMode.IndirectX, AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.ZeroPage,  AddressingMode.Implied, AddressingMode.Immediate, AddressingMode.Implied,     AddressingMode.Immediate, AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  AddressingMode.Absolute,  // 0xE?
        AddressingMode.Relative,  AddressingMode.IndirectY, AddressingMode.NotAvailable, AddressingMode.IndirectY, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.ZeroPageX, AddressingMode.Implied, AddressingMode.AbsoluteY, AddressingMode.Implied,     AddressingMode.AbsoluteY, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, AddressingMode.AbsoluteX, // 0xF?
    };

    public static enum PageCrossingMode
    {
        NormalPageCrossing,
        BranchPageCrossing,
        NoPageCrossing,
        NotAvailable;
    }

    public static final PageCrossingMode[] OPPAGECROSSING =
    {
    //                0x?0                                 0x?1                                 0x?2                             0x?3                                 0x?4                             0x?5                             0x?6                             0x?7                             0x?8                             0x?9                                 0x?A                             0x?B                             0x?C                                 0x?D                                 9x?E                                 0x?F
        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x0?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x1?
        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x2?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x3?

        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x4?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x5?
        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x6?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x7?

        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x8?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0x9?
        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0xA?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NormalPageCrossing, // 0xB?

        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0xC?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0xD?
        PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0xE?
        PageCrossingMode.BranchPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NotAvailable,   PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NormalPageCrossing, PageCrossingMode.NoPageCrossing,     PageCrossingMode.NoPageCrossing,     // 0xF?
    };

    public static final int OPCYCLES[] =
    {
        7, 6, 2, 8, 3, 3, 5, 5, 3, 2, 2, 2, 4, 4, 6, 6, // 0x0?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7, // 0x1?
        6, 6, 2, 8, 3, 3, 5, 5, 4, 2, 2, 2, 4, 4, 6, 6, // 0x2?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7, // 0x3?

        6, 6, 2, 8, 3, 3, 5, 5, 3, 2, 2, 2, 3, 4, 6, 6, // 0x4?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7, // 0x5?
        6, 6, 2, 8, 3, 3, 5, 5, 4, 2, 2, 2, 5, 4, 6, 6, // 0x6?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7, // 0x7?

        2, 6, 2, 6, 3, 3, 3, 3, 2, 2, 2, 2, 4, 4, 4, 4, // 0x8?
        2, 6, 2, 6, 4, 4, 4, 4, 2, 5, 2, 5, 5, 5, 5, 5, // 0x9?
        2, 6, 2, 6, 3, 3, 3, 4, 2, 2, 2, 2, 4, 4, 4, 4, // 0xA?
        2, 5, 2, 5, 4, 4, 4, 4, 2, 4, 2, 4, 4, 4, 4, 4, // 0xB?

        2, 6, 2, 8, 3, 3, 5, 5, 2, 2, 2, 2, 4, 4, 6, 6, // 0xC?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7, // 0xD?
        2, 6, 2, 8, 3, 3, 5, 5, 2, 2, 2, 2, 4, 4, 6, 6, // 0xE?
        2, 5, 2, 8, 4, 4, 6, 6, 2, 4, 2, 7, 4, 4, 7, 7  // 0xF?
    };

    public static final String OPNAMES[] =
    {
        "BRK", "ORA", "N/A", "---", "---", "ORA", "ASL", "---", "PHP", "ORA", "ASL", "ANC", "---", "ORA", "ASL", "---", // 0
        "BPL", "ORA", "N/A", "---", "---", "ORA", "ASL", "---", "CLC", "ORA", "---", "---", "---", "ORA", "ASL", "---", // 1
        "JSR", "AND", "N/A", "---", "BIT", "AND", "ROL", "---", "PLP", "AND", "ROL", "ANC", "BIT", "AND", "ROL", "---", // 2
        "BMI", "AND", "N/A", "---", "---", "AND", "ROL", "---", "SEC", "AND", "---", "---", "---", "AND", "ROL", "---", // 3
        "RTI", "EOR", "N/A", "---", "---", "EOR", "LSR", "---", "PHA", "EOR", "LSR", "ALR", "JMP", "EOR", "LSR", "---", // 4
        "BVC", "EOR", "N/A", "---", "---", "EOR", "LSR", "---", "CLI", "EOR", "---", "---", "---", "EOR", "LSR", "---", // 5
        "RTS", "ADC", "N/A", "---", "---", "ADC", "ROR", "---", "PLA", "ADC", "ROR", "ARR", "JMP", "ADC", "ROR", "---", // 6
        "BVS", "ADC", "N/A", "---", "---", "ADC", "ROR", "---", "SEI", "ADC", "---", "---", "---", "ADC", "ROR", "---", // 7
        "---", "STA", "---", "SAX", "STY", "STA", "STX", "SAX", "DEY", "STA", "TXA", "---", "STY", "STA", "STX", "SAX", // 8
        "BCC", "STA", "N/A", "---", "STY", "STA", "STX", "SAX", "TYA", "STA", "TXS", "---", "---", "STA", "---", "---", // 9
        "LDY", "LDA", "LDX", "LAX", "LDY", "LDA", "LDX", "LAX", "TAY", "LDA", "TAX", "---", "LDY", "LDA", "LDX", "LAX", // A
        "BCS", "LDA", "N/A", "LAX", "LDY", "LDA", "LDX", "LAX", "CLV", "LDA", "TSX", "LAS", "LDY", "LDA", "LDX", "LAX", // B
        "CPY", "CMP", "---", "---", "CPY", "CMP", "DEC", "---", "INY", "CMP", "DEX", "AXS", "CPY", "CMP", "DEC", "---", // C
        "BNE", "CMP", "N/A", "---", "---", "CMP", "DEC", "---", "CLD", "CMP", "---", "---", "---", "CMP", "DEC", "---", // D
        "CPX", "SBC", "---", "---", "CPX", "SBC", "INC", "---", "INX", "SBC", "NOP", "SBC", "CPX", "SBC", "INC", "---", // E
        "BEQ", "SBC", "N/A", "---", "---", "SBC", "INC", "---", "SED", "SBC", "---", "---", "---", "SBC", "INC", "---"  // F
    };
}