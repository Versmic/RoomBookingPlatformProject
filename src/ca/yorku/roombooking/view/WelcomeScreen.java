package ca.yorku.roombooking.view;

import javax.swing.*;           // JPanel, JButton, JLabel, Timer, BoxLayout, etc.
import java.awt.*;              // Color, Graphics, Graphics2D, Font, layouts, AlphaComposite
import java.awt.event.*;        // MouseAdapter / MouseEvent for hover effects
import java.awt.geom.*;         // Ellipse2D, RoundRectangle2D for custom shapes
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * First screen the user sees.
 * initially shows Log In / Sign Up / Continue as Guest
 * clicking Sign Up shrinks those three out, then pops in the account
 * type choices: Student / Staff / Faculty / Partner (+ a Back button)
*/
public class WelcomeScreen extends JPanel {

    // Reference to the parent frame so buttons can trigger screen navigation.
    private final MainFrame mainFrame;

    private final JPanel inner;

    // The subtitle label is kept as a field so we can change its text when
    // switching between "auth" mode and "choose account type" mode.
    private final JLabel subtitleLabel;

    // buttonArea is the container whose contents we swap out - it always
    // holds whichever set of RoundedButtons is currently active (either the
    // 3 auth buttons, or the 4 account type buttons + back button).
    private final JPanel buttonArea;

    // Tracks how "faded in" the background currently is (0 = invisible, 1 = fully visible).
    private float contentAlpha = 0f;

    public WelcomeScreen(MainFrame mainFrame) {

        this.mainFrame = mainFrame; // hold a reference to the main frame

        this.setLayout(new GridBagLayout()); // welcome screen panel uses grid bag layout
        this.setOpaque(true);                // welcome screen panel has solid background

        // create inner panel holds the text + buttons and stacked vertically
        inner = new JPanel(); // create an inner panel
        inner.setOpaque(false); // let welcome screen painted background show through
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS)); // stack items top to bottom


        // title text
        JLabel title = new JLabel("Welcome to RoomBooking");
        title.setFont(new Font("SansSerif", Font.BOLD, 26)); // text set to bold and size 26
        title.setForeground(Colours.TEXT_LIGHT);                      // set color to TEXT_LIGHT, init above
        title.setAlignmentX(Component.LEFT_ALIGNMENT);        // align title text to left


        // subtitle text - text changes depending on auth mode vs account-type mode
        subtitleLabel = new JLabel("Start with sign up or sign in");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14)); // text set to plain and size 14
        subtitleLabel.setForeground(Colours.TEXT_MUTED); // set color to TEXT_MUTED, init above
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // align subtitle text to left



        // add the two labels to the inner panel with a small gap between them
        inner.add(title);
        inner.add(Box.createVerticalStrut(8)); // 8px vertical spacer
        inner.add(subtitleLabel);
        //inner.add(Box.createVerticalGlue());   // flexible empty space - pushes buttons to the bottom

        inner.add(Box.createVerticalStrut(190)); // creates 190px vertical spacer from subtitle text

        // buttonArea holds whichever button set is currently showing. Using a
        // dedicated sub-panel (instead of adding buttons straight to inner)
        // means we can removeAll()/rebuild just this part when switching modes,
        // without disturbing the title/subtitle above it.
        buttonArea = new JPanel();
        buttonArea.setOpaque(false);
        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.Y_AXIS));
        buttonArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        inner.add(buttonArea);

        buildAuthButtons(); // build log in, sign up, guest in buttonArea

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;                 // inner panel stretches to fill available space
        gbc.weightx = 1;                                    // give it all available horizontal space
        gbc.weighty = 1;                                    // give it all available vertical space
        gbc.insets = new Insets(60, 36, 50, 36);             // top, left, bottom, right padding in pixels
        add(inner, gbc);
    }

    // -----------------------------------------------------------------
    // BUTTON SET BUILDERS
    // Each of these clears nothing itself - they just create buttons and
    // add them into buttonArea. transitionButtonArea() is what clears
    // buttonArea before calling one of these.
    // -----------------------------------------------------------------

    // build log in, sign up, guest in buttonArea
    private void buildAuthButtons() {
        // login = solid purple to blue gradient + white text
        RoundedButton loginBtn = new RoundedButton("LOG IN", Colours.PURPLE, Colours.BLUE, Colours.TEXT_LIGHT, ButtonStyle.GRADIENT_FILL);
        // sign up = solid turquoise to dark teal gradient + white text
        RoundedButton signUpBtn = new RoundedButton("SIGN UP", Colours.TURQUOISE, Colours.DARK_TEAL, Colours.TEXT_LIGHT, ButtonStyle.GRADIENT_FILL);
        // Guest: subtle low-emphasis button, coral text as a small palette accent
        RoundedButton guestBtn = new RoundedButton("CONTINUE AS GUEST", Color.decode("#0D8FA5"), Color.decode("#4A74D9"), Colours.TEXT_LIGHT, ButtonStyle.GRADIENT_FILL);

        // align all buttons to left then stretch full width + 48px height
        for (RoundedButton b : new RoundedButton[]{loginBtn, signUpBtn, guestBtn}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }

        // add action listener to login button
        loginBtn.addActionListener(e -> {
            // mainFrame.navigateTo(new LoginScreen(mainFrame)); // <- uncomment when LoginScreen exists
            System.out.println("Log In clicked - wire up LoginScreen here");
        });

        // when sign up button is pressed, remove all current
        // buttons and instead display account type buttons
        signUpBtn.addActionListener(e -> showAccountTypeButtons());

        // add action listener to guest button
        guestBtn.addActionListener(e -> {
            // mainFrame.navigateTo(new GuestHomeScreen(mainFrame)); // <- uncomment when that screen exists
            System.out.println("Continue as Guest clicked - wire up guest flow here");
        });

        // add buttons to the button area + 14px gaps between them
        buttonArea.add(loginBtn);
        buttonArea.add(Box.createVerticalStrut(14));
        buttonArea.add(signUpBtn);
        buttonArea.add(Box.createVerticalStrut(14));
        buttonArea.add(guestBtn);
    }

    /** Builds the Student / Staff / Faculty / Partner + Back buttons. */
    private void buildAccountTypeButtons() {
        // each account type gets its own accent color from the same palette,
        // rendered as an outline style so this screen reads as a step "into"
        // the flow rather than a repeat of the first screen
        RoundedButton studentBtn = new RoundedButton("STUDENT", Colours.PURPLE, Colours.PURPLE, Colours.PURPLE, ButtonStyle.OUTLINE);
        RoundedButton staffBtn   = new RoundedButton("STAFF", Colours.BLUE, Colours.BLUE, Colours.BLUE, ButtonStyle.OUTLINE);
        RoundedButton facultyBtn = new RoundedButton("FACULTY", Colours.TURQUOISE, Colours.TURQUOISE, Colours.TURQUOISE, ButtonStyle.OUTLINE);
        RoundedButton partnerBtn = new RoundedButton("PARTNER", Colours.DARK_TEAL, Colours.DARK_TEAL, Colours.DARK_TEAL, ButtonStyle.OUTLINE);
        // low-emphasis back link so the user isn't stuck on this screen
        RoundedButton backBtn = new RoundedButton("\u2190 BACK", new Color(0x33, 0x3E, 0x55), new Color(0x33, 0x3E, 0x55), Colours.TEXT_MUTED, ButtonStyle.SUBTLE_FILL);

        for (RoundedButton b : new RoundedButton[]{studentBtn, staffBtn, facultyBtn, partnerBtn}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        }
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36)); // slightly shorter - it's a secondary action

        // TODO: replace each println with real navigation once the per-type sign up screens exist, e.g.:
        // mainFrame.navigateTo(new StudentSignUpScreen(mainFrame));
        studentBtn.addActionListener(e -> System.out.println("Student selected - wire up StudentSignUpScreen here"));
        staffBtn.addActionListener(e -> System.out.println("Staff selected - wire up StaffSignUpScreen here"));
        facultyBtn.addActionListener(e -> System.out.println("Faculty selected - wire up FacultySignUpScreen here"));
        partnerBtn.addActionListener(e -> System.out.println("Partner selected - wire up PartnerSignUpScreen here"));
        backBtn.addActionListener(e -> showAuthButtons()); // reverse the transition back to Log In / Sign Up / Guest

        buttonArea.add(studentBtn);
        buttonArea.add(Box.createVerticalStrut(14));
        buttonArea.add(staffBtn);
        buttonArea.add(Box.createVerticalStrut(14));
        buttonArea.add(facultyBtn);
        buttonArea.add(Box.createVerticalStrut(14));
        buttonArea.add(partnerBtn);
        buttonArea.add(Box.createVerticalStrut(20)); // slightly bigger gap before the back link
        buttonArea.add(backBtn);
    }

    // -----------------------------------------------------------------
    // MODE SWITCHING
    // -----------------------------------------------------------------

    /** Switches from the auth buttons to the account type choices. */
    private void showAccountTypeButtons() {
        subtitleLabel.setText("Choose your account type");
        transitionButtonArea(this::buildAccountTypeButtons);
    }

    /** Switches back from the account type choices to the auth buttons. */
    private void showAuthButtons() {
        subtitleLabel.setText("Start with sign up or sign in");
        transitionButtonArea(this::buildAuthButtons);
    }

    /**
     * Shrinks out whatever buttons are currently in buttonArea, then - only
     * once that's genuinely finished - clears buttonArea, runs buildNewButtons
     * to populate it with the next set, and pops those in.
     *
     * @param buildNewButtons a method reference to buildAuthButtons() or
     *                        buildAccountTypeButtons() - whichever should
     *                        appear after the shrink-out completes
     */
    private void transitionButtonArea(Runnable buildNewButtons) {
        List<RoundedButton> outgoing = currentButtons();
        Collections.reverse(outgoing); // shrink from the bottom button upward - reads nicely as a "collapse"

        shrinkOutStaggered(outgoing, 50, () -> {
            // this callback only runs once the LAST outgoing button has
            // fully finished its own shrink+fade - guaranteed no overlap
            buttonArea.removeAll();
            buildNewButtons.run();
            buttonArea.revalidate();
            buttonArea.repaint();
            popInStaggered(currentButtons(), 60);
        });
    }

    /** Returns whatever RoundedButtons currently live in buttonArea, in order. */
    private List<RoundedButton> currentButtons() {
        List<RoundedButton> list = new ArrayList<>();
        for (Component c : buttonArea.getComponents()) {
            if (c instanceof RoundedButton rb) list.add(rb);
        }
        return list;
    }

    /**
     * Plays each button's shrink+fade exit animation, staggered by staggerMs.
     * Buttons are disabled immediately so they can't be clicked mid-transition.
     * onAllDone fires exactly once, when the last (most-delayed) button's own
     * exit animation completes - not based on a manually-guessed total time.
     */
    private void shrinkOutStaggered(List<RoundedButton> buttons, int staggerMs, Runnable onAllDone) {
        if (buttons.isEmpty()) {
            if (onAllDone != null) onAllDone.run();
            return;
        }
        for (RoundedButton b : buttons) b.setEnabled(false); // block clicks while animating out

        int lastIndex = buttons.size() - 1;
        for (int i = 0; i < buttons.size(); i++) {
            RoundedButton b = buttons.get(i);
            boolean isLast = (i == lastIndex);
            Timer startDelay = new Timer(staggerMs * i, e -> b.playExitAnimation(isLast ? onAllDone : null));
            startDelay.setRepeats(false);
            startDelay.start();
        }
    }

    /** Plays each button's pop-in (scale+fade) entrance animation, staggered by staggerMs. */
    private void popInStaggered(List<RoundedButton> buttons, int staggerMs) {
        for (int i = 0; i < buttons.size(); i++) {
            RoundedButton b = buttons.get(i);
            Timer startDelay = new Timer(staggerMs * i, e -> b.playEntranceAnimation());
            startDelay.setRepeats(false);
            startDelay.start();
        }
    }

    /**
     * Plays the whole screen's initial entrance sequence:
     *   1) the background glow fades in over ~200ms
     *   2) whichever buttons are currently in buttonArea (the auth buttons,
     *      on first launch) pop in staggered, same as after any later transition.
     * Call this once after the screen is added to a visible frame.
     */
    public void playEntranceAnimation() {
        // ---- 1) fade the background glow in ----
        contentAlpha = 0f;                 // start fully transparent
        Timer bgTimer = new Timer(15, null); // fires every 15ms (~66 times per second)
        bgTimer.addActionListener(e -> {
            contentAlpha += 0.07f;         // step the alpha value up a little each tick (~200ms total)
            if (contentAlpha >= 1f) {      // once fully visible...
                contentAlpha = 1f;         // clamp so it doesn't overshoot 1.0
                bgTimer.stop();            // ...stop the timer so it's not running forever
            }
            repaint(); // ask Swing to redraw this panel (calls paintComponent below)
        });
        bgTimer.start(); // kick off the background fade

        // ---- 2) pop the current buttons in, staggered ----
        popInStaggered(currentButtons(), 70);
    }

    /**
     * Custom painting for the background: solid navy fill + three soft,
     * semi-transparent glow circles in purple/blue/cyan layered like the
     * Fairy palette swatches. The whole thing fades in using contentAlpha.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // let Swing do its normal background clearing first

        // Graphics2D gives us access to shapes, gradients, and anti-aliasing
        // that the basic Graphics class doesn't expose. g.create() makes a
        // throwaway copy so our settings don't leak into other painting.
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // smooth curved edges

        // apply the fade-in: everything drawn with g2 from this point on is
        // multiplied by contentAlpha (0 = invisible, 1 = fully opaque)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, contentAlpha));

        int w = getWidth();  // current panel width in pixels
        int h = getHeight(); // current panel height in pixels

        // ---- base background: solid dark navy ----
        g2.setColor(Colours.BG_NAVY);
        g2.fillRect(0, 0, w, h);

        // ---- glow shape 1: purple, top-left-ish, low opacity ----
        // new Color(r, g, b, alpha) - alpha (0-255) controls transparency, 255 = fully opaque
        g2.setColor(new Color(Colours.PURPLE.getRed(), Colours.PURPLE.getGreen(), Colours.PURPLE.getBlue(), 70));
        g2.fill(new Ellipse2D.Double(-w * 0.25, -h * 0.10, w * 0.9, w * 0.9));

        // ---- glow shape 2: blue, upper-right, slightly more opaque ----
        g2.setColor(new Color(Colours.BLUE.getRed(), Colours.BLUE.getGreen(), Colours.BLUE.getBlue(), 90));
        g2.fill(new Ellipse2D.Double(w * 0.45, -h * 0.05, w * 0.85, w * 0.85));

        // ---- glow shape 3: cyan, lower area, drawn with a gradient for depth ----
        // GradientPaint blends smoothly from one color at one point to another
        // color at another point, instead of a flat fill.
        GradientPaint gradient = new GradientPaint(
                0, (float) (h * 0.75), Colours.CYAN,   // start point + start color
                w, h, Colours.BLUE                      // end point + end color
        );
        g2.setPaint(gradient);
        g2.fill(new Ellipse2D.Double(w * 0.10, h * 0.80, w * 0.95, w * 0.95));

        g2.dispose(); // release the Graphics2D copy we created above
    }

    /** Which visual style a RoundedButton should render as. */
    private enum ButtonStyle {
        GRADIENT_FILL, // solid two-color gradient background (primary action)
        OUTLINE,       // transparent fill, colored border + text (secondary action)
        SUBTLE_FILL    // flat low-contrast fill (tertiary / low-emphasis action)
    }

    /**
     * A pill-shaped button that supports gradient fill, outline, or subtle
     * fill styles, an animated hover brighten/darken effect, and both a
     * scale+fade "pop in" entrance animation and its mirror-image "shrink
     * out" exit animation (matching the search bar animation from the
     * reference clip: grows/shrinks between 85% and 100% width while
     * fading, anchored at its own horizontal center, ~250ms).
     */
    static class RoundedButton extends JButton {
        private final Color colorA;   // first color (gradient start, or the only fill color)
        private final Color colorB;   // second color (gradient end, or border color for OUTLINE)
        private final ButtonStyle style;
        private float hoverProgress = 0f; // 0 = not hovered, 1 = fully hovered
        private Timer hoverTimer;

        // entranceProgress goes from 0 (shrunk + invisible) to 1 (fully settled).
        // Defaults to 1 so a button behaves normally if no animation is ever played on it.
        private float entranceProgress = 1f;
        private Timer entranceTimer;

        RoundedButton(String text, Color colorA, Color colorB, Color textColor, ButtonStyle style) {
            super(text);
            this.colorA = colorA;
            this.colorB = colorB;
            this.style = style;

            setContentAreaFilled(false);   // we paint the background ourselves
            setFocusPainted(false);        // no default focus rectangle
            setBorderPainted(false);       // no default border
            setForeground(textColor);      // text color passed in per-button
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // pointer cursor on hover

            // mouse listener drives the hover animation
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    animateHoverTo(1f); // animate hoverProgress towards "fully hovered"
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    animateHoverTo(0f); // animate hoverProgress back towards "not hovered"
                }
            });
        }


        /** Smoothly animates hoverProgress from its current value to target (0 or 1). */

        private void animateHoverTo(float target) {
            if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop(); // cancel any in-flight animation
            hoverTimer = new Timer(10, null); // fires every 10ms
            hoverTimer.addActionListener(e -> {
                // move hoverProgress 25% of the remaining distance towards target each tick
                hoverProgress += (target - hoverProgress) * 0.25f;
                if (Math.abs(target - hoverProgress) < 0.01f) { // close enough - snap and stop
                    hoverProgress = target;
                    hoverTimer.stop();
                }
                repaint(); // redraw with the new hoverProgress value
            });
            hoverTimer.start();
        }

        /** Plays the "pop in" entrance animation with no completion callback. */
        public void playEntranceAnimation() {
            playEntranceAnimation(null);
        }

        /**
         * Plays the "pop in" entrance animation: scales up from 85% to 100%
         * width while fading from transparent to opaque, over ~250ms, using
         * an ease-out curve. Mirrors the search bar animation from the
         * reference clip. onComplete (if not null) fires once the animation finishes.
         */
        public void playEntranceAnimation(Runnable onComplete) {
            runProgressAnimation(true, onComplete);
        }

        /**
         * Plays the "shrink out" exit animation: the reverse of the entrance -
         * scales down from 100% to 85% width while fading out, over ~250ms,
         * using an ease-in curve (accelerating away, rather than decelerating
         * into place - this is what makes an exit read differently from an entrance).
         * onComplete (if not null) fires once the animation finishes.
         */
        public void playExitAnimation(Runnable onComplete) {
            runProgressAnimation(false, onComplete);
        }

        /**
         * Shared driver for both playEntranceAnimation and playExitAnimation -
         * they're the same scale+fade mechanic just running in opposite
         * directions with different easing curves.
         */
        private void runProgressAnimation(boolean isEntrance, Runnable onComplete) {
            if (entranceTimer != null && entranceTimer.isRunning()) entranceTimer.stop();

            long startTime = System.currentTimeMillis();
            int durationMs = 250; // total animation length - matches the clip's quick, snappy timing
            float startValue = isEntrance ? 0f : 1f; // entrance starts shrunk, exit starts fully shown
            float endValue = isEntrance ? 1f : 0f;

            entranceProgress = startValue;
            entranceTimer = new Timer(12, null); // ~83 ticks per second while animating
            entranceTimer.addActionListener(e -> {
                long elapsed = System.currentTimeMillis() - startTime;
                float t = Math.min(1f, elapsed / (float) durationMs); // 0 -> 1 raw progress through the animation
                float eased = isEntrance ? easeOutCubic(t) : easeInCubic(t); // different feel for in vs out
                entranceProgress = startValue + (endValue - startValue) * eased;
                repaint();
                if (t >= 1f) {
                    entranceTimer.stop();
                    if (onComplete != null) onComplete.run(); // signal completion so callers can chain the next step
                }
            });
            entranceTimer.start();
        }

        /**
         * Ease-out cubic curve: starts fast, decelerates smoothly into place
         * with no bounce/overshoot - used for entrances.
         */
        private float easeOutCubic(float t) {
            float f = t - 1f;
            return f * f * f + 1f;
        }

        /**
         * Ease-in cubic curve: starts slow, accelerates away - used for exits,
         * so leaving reads as a distinct motion from arriving.
         */
        private float easeInCubic(float t) {
            return t * t * t;
        }

        /** Linearly blends two colors together by the given 0-1 ratio. */
        private Color blend(Color c1, Color c2, float ratio) {
            int r = (int) (c1.getRed()   + (c2.getRed()   - c1.getRed())   * ratio);
            int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
            int b = (int) (c1.getBlue()  + (c2.getBlue()  - c1.getBlue())  * ratio);
            return new Color(r, g, b);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = h; // using the full height as the corner arc makes the ends fully rounded (pill shape)

            // ---- entrance/exit animation: scale horizontally from center + fade ----
            // entranceProgress works the same way whether we're popping in or
            // shrinking out - it's just a 0..1 value that this block turns
            // into a horizontal scale + alpha. Only applied while mid-animation
            // (< 1) so a settled button's repaints (e.g. hover) stay cheap.
            if (entranceProgress < 1f) {
                float scaleX = 0.85f + 0.15f * entranceProgress; // 85% width at progress 0 -> 100% at progress 1
                int centerX = w / 2;                             // anchor point for the scale (horizontal center)

                g2.translate(centerX, 0);   // move origin to the button's horizontal center
                g2.scale(scaleX, 1.0);      // scale ONLY horizontally - height stays constant, matching the clip
                g2.translate(-centerX, 0);  // move origin back so drawing coordinates are unchanged

                // fade tracks the same progress value
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, entranceProgress))));
            }

            // hover makes filled buttons slightly brighter and outline buttons slightly more filled-in
            switch (style) {
                case GRADIENT_FILL -> {
                    // brighten both gradient colors a touch based on hoverProgress
                    Color a = blend(colorA, Color.WHITE, hoverProgress * 0.2f);
                    Color b = blend(colorB, Color.WHITE, hoverProgress * 0.2f);
                    GradientPaint gp = new GradientPaint(0, 0, a, w, 0, b); // left-to-right gradient
                    g2.setPaint(gp);
                    g2.fill(new RoundRectangle2D.Double(0, 0, w, h, arc, arc));
                }
                case OUTLINE -> {
                    // background fades from transparent to a faint tint of colorA on hover
                    int alpha = (int) (hoverProgress * 40); // 0 -> 40 out of 255
                    g2.setColor(new Color(colorA.getRed(), colorA.getGreen(), colorA.getBlue(), alpha));
                    g2.fill(new RoundRectangle2D.Double(0, 0, w, h, arc, arc));

                    g2.setColor(colorB); // border color
                    g2.setStroke(new BasicStroke(2f));
                    g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, arc, arc));
                }
                case SUBTLE_FILL -> {
                    // slightly lighten the flat fill color on hover
                    Color fill = blend(colorA, Color.WHITE, hoverProgress * 0.15f);
                    g2.setColor(fill);
                    g2.fill(new RoundRectangle2D.Double(0, 0, w, h, arc, arc));
                }
            }

            // paint the button's text using g2 (not the original g) so it shares
            // the same transform + fade as the box we just drew above - this is
            // what makes the text pop/shrink together with the background.
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}