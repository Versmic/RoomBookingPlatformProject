package roombooking.view;

import java.awt.Color;

public final class Colours {

    private Colours() {
        // Prevent objects of this class from being created
    }
    
    static final Color BG_NAVY   = new Color(0x25, 0x30, 0x45); // dark navy app background
    static final Color PURPLE    = Color.decode("#A163F7"); // primary accent (glow + login button)
    static final Color BLUE      = Color.decode("#6F88FC"); // secondary accent (gradient partner)
    static final Color CYAN      = Color.decode("#45E3FF"); // tertiary accent (sign up outline)
    static final Color CORAL     = Color.decode("#FF7582"); // small highlight accent (used sparingly)
    static final Color TEXT_LIGHT = Color.decode("#F5F6FA"); // near-white text for headings and primary labels
    static final Color TEXT_MUTED = Color.decode("#B7C0D6"); // soft grey-blue text for subtitles and secondary text
    static final Color DARK_TEAL = Color.decode("#076572"); // deep teal
    static final Color TEAL      = Color.decode("#008496"); // rich teal
    static final Color TURQUOISE = Color.decode("#09AA91"); // vibrant turquoise
    static final Color GREEN     = Color.decode("#44C876"); // fresh green 
    static final Color LIME      = Color.decode("#CCE574"); // soft lime
}