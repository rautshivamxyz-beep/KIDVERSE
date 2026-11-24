package com.kidverse.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {
    private static final int INK = 0xFF202044;
    private static final int MUTED = 0xFF77758F;
    private static final int PURPLE = 0xFF6256D9;
    private static final int BG = 0xFFF7F6FC;
    private static final int WHITE = 0xFFFFFFFF;

    private LinearLayout root;
    private LinearLayout content;
    private SharedPreferences prefs;
    private int stars;
    private int mathIndex = 0;
    private int storyIndex = 0;
    private int scienceIndex = 0;
    private int earthIndex = 0;
    private String activeTab = "Home";
    private DrawingCanvas drawingCanvas;

    private final String[] mathQuestions = {
        "36 + 27 = ?", "82 - 45 = ?", "6 × 7 = ?", "Half of 54 is?",
        "What is 3/4 of 20?", "125 + 75 = ?", "9 × 8 = ?", "100 - 38 = ?",
        "A book has 64 pages. You read 28. How many remain?", "4 groups of 12 make?"
    };
    private final String[][] mathOptions = {
        {"53", "63", "73"}, {"37", "47", "27"}, {"42", "48", "36"},
        {"27", "24", "29"}, {"10", "15", "18"}, {"190", "200", "210"},
        {"72", "81", "63"}, {"72", "62", "68"}, {"36", "38", "34"}, {"36", "48", "42"}
    };
    private final int[] mathAnswers = {1, 0, 0, 0, 1, 1, 0, 1, 1, 1};
    private final String[] mathHints = {
        "Add tens and ones: 30 + 20 = 50, then 6 + 7 = 13. Total: 63.",
        "82 − 40 = 42, then take away 5 more. The answer is 37.",
        "Six groups of seven: 7 + 7 + 7 + 7 + 7 + 7 = 42.",
        "Half means two equal groups. 27 + 27 = 54.",
        "One quarter of 20 is 5. Three quarters is 5 + 5 + 5 = 15.",
        "125 + 75 makes 200. You can bridge through 150.",
        "Nine groups of eight make 72. Try 10 × 8 − 8.",
        "100 − 30 = 70, then 70 − 8 = 62.",
        "Subtract the pages read: 64 − 28 = 36 pages.",
        "12 + 12 + 12 + 12 = 48."
    };
    private final String[] storyTitles = {
        "Mira and the Moon Garden", "The Helpful Robot", "The River That Sang",
        "Asha's Tiny Telescope", "The Mango Tree Promise"
    };
    private final String[] storyTexts = {
        "Mira loved looking at the night sky. One evening, she planted tiny seeds beside her window. Every night she watered them and watched the moon. Soon, bright silver flowers opened. Mira learned that patience helps little things grow.",
        "Robo was a small robot who lived in a sunny town. One day, he saw a bird with a tangled kite string. Robo gently helped the bird, then recycled the string. Everyone learned that being kind also means caring for nature.",
        "A river near Tara's village used to sparkle. One morning she noticed wrappers caught between the stones. She invited her friends to make signs asking everyone to keep the river clean. They never entered the water; they worked safely with adults along the bank. Soon the river flowed freely again. Tara learned that small, safe actions can help a whole community.",
        "Asha borrowed a tiny telescope from her grandfather. She looked at the Moon and drew its bright and dark patches. Grandfather explained that the Moon does not make its own light; it reflects sunlight. Asha made a notebook of questions and discovered that careful observation is the beginning of science.",
        "Every summer, Kabir's family cared for the old mango tree. They watered its roots during dry weeks and left fallen leaves to become compost. One year the tree gave more fruit than ever. Kabir shared the mangoes with neighbours and learned that caring for nature is a promise we keep over time."
    };
    private final String[] scienceQuestions = {
        "Which part of a plant takes in most water?", "What causes day and night on Earth?",
        "Which material is attracted to a magnet?", "What do we call water vapour cooling into tiny drops?",
        "Which animal is a mammal?"
    };
    private final String[][] scienceOptions = {
        {"Flower", "Roots", "Fruit"}, {"Earth spinning on its axis", "The Moon moving", "Clouds moving"},
        {"Wood", "Plastic", "Iron"}, {"Evaporation", "Condensation", "Melting"},
        {"Frog", "Dolphin", "Butterfly"}
    };
    private final int[] scienceAnswers = {1, 0, 2, 1, 1};
    private final String[] scienceHints = {
        "Roots absorb water from the soil.",
        "Earth rotates once about every 24 hours. The side facing the Sun has day.",
        "Iron is a metal that magnets can attract.",
        "Condensation happens when water vapour cools and forms tiny liquid drops.",
        "Dolphins are mammals: they breathe air and feed their young milk."
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("kidverse_progress", MODE_PRIVATE);
        stars = prefs.getInt("stars", 0);
        Window window = getWindow();
        window.setStatusBarColor(BG);
        window.setNavigationBarColor(WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        showHome();
        UpdateManager.checkForUpdate(this, false);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private GradientDrawable gradient(int first, int second, int radius) {
        GradientDrawable drawable = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR, new int[]{first, second});
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setFontFeatureSettings("kern");
        view.setTypeface(Typeface.create("sans-serif-rounded", bold ? Typeface.BOLD : Typeface.NORMAL));
        view.setIncludeFontPadding(true);
        return view;
    }

    private void space(int height) {
        View spacer = new View(this);
        content.addView(spacer, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private void screen(String tab) {
        activeTab = tab;
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(20), dp(12), dp(20), dp(12));

        TextView brand = label("✦  KIDVERSE", 20, INK, true);
        top.addView(brand, new LinearLayout.LayoutParams(0, dp(42), 1));

        TextView starPill = label("⭐  " + stars, 14, 0xFF5948B5, true);
        starPill.setGravity(Gravity.CENTER);
        starPill.setPadding(dp(14), 0, dp(14), 0);
        starPill.setBackground(shape(0xFFECE9FF, 22));
        top.addView(starPill, new LinearLayout.LayoutParams(-2, dp(38)));
        root.addView(top, new LinearLayout.LayoutParams(-1, -2));

        ScrollView scroll = new ScrollView(this);
        scroll.setClipToPadding(false);
        scroll.setFillViewport(true);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(8), dp(18), dp(24));
        scroll.addView(content, new ScrollView.LayoutParams(-1, -2));
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        addBottomNavigation();
    }

    private void addBottomNavigation() {
        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER);
        bar.setPadding(dp(8), dp(7), dp(8), dp(8));
        bar.setBackgroundColor(WHITE);
        String[] names = {"Home", "Learn", "Create", "Parent"};
        String[] icons = {"⌂", "✦", "✎", "☻"};
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            boolean selected = name.equals(activeTab);
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setPadding(0, dp(5), 0, dp(3));
            TextView icon = label(icons[i], 20, selected ? PURPLE : 0xFF9996AA, true);
            icon.setGravity(Gravity.CENTER);
            TextView title = label(name, 11, selected ? PURPLE : MUTED, selected);
            title.setGravity(Gravity.CENTER);
            item.addView(icon, new LinearLayout.LayoutParams(-1, dp(25)));
            item.addView(title, new LinearLayout.LayoutParams(-1, dp(18)));
            if (selected) item.setBackground(shape(0xFFF0EEFF, 14));
            bar.addView(item, new LinearLayout.LayoutParams(0, dp(52), 1));
            item.setOnClickListener(v -> openTab(name));
        }
        root.addView(bar, new LinearLayout.LayoutParams(-1, -2));
    }

    private void openTab(String tab) {
        if ("Home".equals(tab)) showHome();
        else if ("Learn".equals(tab)) showLearn();
        else if ("Create".equals(tab)) showCreate();
        else showParentGate();
    }

    private void intro(String title, String subtitle) {
        TextView heading = label(title, 26, INK, true);
        content.addView(heading);
        TextView sub = label(subtitle, 14, MUTED, false);
        sub.setPadding(0, dp(4), 0, dp(8));
        content.addView(sub);
    }

    private void smallHeading(String value) {
        TextView heading = label(value, 17, INK, true);
        heading.setPadding(dp(2), dp(2), 0, dp(10));
        content.addView(heading);
    }

    private void action(String title, String subtitle, String icon, int color,
                        String buttonText, Runnable click) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(15), dp(16), dp(15));
        card.setBackground(shape(color, 22));
        card.setElevation(dp(1));
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(-1, -2);
        cardParams.bottomMargin = dp(12);
        content.addView(card, cardParams);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView emoji = label(icon, 27, INK, false);
        emoji.setGravity(Gravity.CENTER);
        emoji.setBackground(shape(0x55FFFFFF, 16));
        top.addView(emoji, new LinearLayout.LayoutParams(dp(52), dp(52)));
        LinearLayout words = new LinearLayout(this);
        words.setOrientation(LinearLayout.VERTICAL);
        words.setPadding(dp(12), 0, 0, 0);
        TextView name = label(title, 17, INK, true);
        TextView desc = label(subtitle, 13, 0xFF5F5B75, false);
        desc.setPadding(0, dp(3), 0, 0);
        words.addView(name);
        words.addView(desc);
        top.addView(words, new LinearLayout.LayoutParams(0, -2, 1));
        card.addView(top);

        TextView open = label(buttonText + "  →", 13, PURPLE, true);
        open.setPadding(dp(2), dp(12), 0, 0);
        card.addView(open);
        card.setOnClickListener(v -> click.run());
        open.setOnClickListener(v -> click.run());
    }

    private TextView button(String value, boolean primary, Runnable click) {
        TextView view = label(value, 14, primary ? WHITE : PURPLE, true);
        view.setGravity(Gravity.CENTER);
        view.setPadding(dp(15), dp(12), dp(15), dp(12));
        view.setBackground(shape(primary ? PURPLE : 0xFFECE9FF, 15));
        view.setClickable(true);
        view.setFocusable(true);
        view.setOnClickListener(v -> click.run());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(9);
        content.addView(view, params);
        return view;
    }

    private void infoCard(String title, String detail, int color) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(15), dp(13), dp(15), dp(13));
        box.setBackground(shape(color, 18));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(10);
        content.addView(box, p);
        box.addView(label(title, 16, INK, true));
        TextView d = label(detail, 13, 0xFF5F5B75, false);
        d.setPadding(0, dp(5), 0, 0);
        box.addView(d);
    }

    private void statRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        String[][] stats = {
            {"⭐", String.valueOf(stars), "Stars"},
            {"🎯", String.valueOf(prefs.getInt("completed", 0)), "Activities"}
        };
        for (String[] stat : stats) {
            LinearLayout box = new LinearLayout(this);
            box.setOrientation(LinearLayout.VERTICAL);
            box.setGravity(Gravity.CENTER);
            box.setPadding(dp(10), dp(10), dp(10), dp(10));
            box.setBackground(shape(WHITE, 17));
            TextView main = label(stat[0] + "  " + stat[1], 18, INK, true);
            main.setGravity(Gravity.CENTER);
            TextView caption = label(stat[2], 12, MUTED, false);
            caption.setGravity(Gravity.CENTER);
            box.addView(main);
            box.addView(caption);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, dp(72), 1);
            p.setMargins(0, 0, dp(9), 0);
            row.addView(box, p);
        }
        content.addView(row);
    }

    private void rewardOnce(String key, int amount) {
        if (!prefs.getBoolean("reward_" + key, false)) {
            stars += amount;
            prefs.edit().putBoolean("reward_" + key, true)
                .putInt("stars", stars)
                .putInt("completed", prefs.getInt("completed", 0) + 1).apply();
        }
    }

    private void showHome() {
        screen("Home");
        intro("Hello, Explorer! 👋", "A little learning. A lot of wonder.");
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(19), dp(18), dp(19), dp(18));
        hero.setBackground(gradient(0xFF6558D8, 0xFF8C73E9, 24));
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, -2);
        hp.bottomMargin = dp(14);
        content.addView(hero, hp);
        TextView heroTag = label("YOUR LEARNING ADVENTURE", 10, 0xFFEAE7FF, true);
        hero.addView(heroTag);
        TextView heroTitle = label("Dream big.\nDiscover more.", 27, WHITE, true);
        heroTitle.setPadding(0, dp(7), 0, dp(5));
        hero.addView(heroTitle);
        hero.addView(label("Play fun missions and grow your world with every new skill.", 13, 0xFFF2F0FF, false));
        TextView heroButton = label("Explore the worlds  →", 13, INK, true);
        heroButton.setGravity(Gravity.CENTER);
        heroButton.setPadding(dp(14), dp(11), dp(14), dp(11));
        heroButton.setBackground(shape(WHITE, 14));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-2, -2);
        bp.topMargin = dp(15);
        hero.addView(heroButton, bp);
        heroButton.setOnClickListener(v -> showLearn());

        statRow();
        space(18);
        smallHeading("Pick your next adventure");
        action("Math Kingdom", "Number puzzles and brain boosts", "🔢", 0xFFFFEBCB,
            "Play maths", this::showMath);
        action("Science Galaxy", "Discover how our world works", "🔬", 0xFFDFF3FF,
            "Explore science", this::showScience);
        action("Story Forest", "Read a tale and imagine more", "📚", 0xFFE8F4DE,
            "Read stories", this::showStory);
        action("Robot City", "Think, plan and guide a robot", "🤖", 0xFFE8E5FF,
            "Build a sequence", this::showRobot);
        action("Creator Studio", "Draw something wonderful", "🎨", 0xFFFFE6F0,
            "Start creating", this::showCreate);
        space(4);
        smallHeading("Your growing world");
        action("Build-a-World", "Use your stars to unlock island rewards", "🏝️", 0xFFE2F2E9,
            "See my island", this::showWorld);
    }

    private void showLearn() {
        screen("Learn");
        intro("Learning worlds", "Choose a world. Try a mission. Earn stars.");
        action("Knowledge Library", "Search topics, read deeper explanations and practise", "📖", 0xFFECE9FF, "Open library", () -> startActivity(new android.content.Intent(this, LibraryActivity.class)));
        action("Math Kingdom", "Addition, subtraction, multiplication and fractions", "🔢", 0xFFFFEBCB, "Start learning", this::showMath);
        action("Science Galaxy", "Plants, space, weather and our amazing planet", "🔬", 0xFFDFF3FF, "Explore science", this::showScience);
        action("Story Forest", "Original stories that grow reading confidence", "📚", 0xFFE8F4DE, "Open stories", this::showStory);
        action("Robot City", "Practice logic by arranging instructions", "🤖", 0xFFE8E5FF, "Guide Robo", this::showRobot);
        action("Explorer Earth", "Learn about forests, oceans and the environment", "🌍", 0xFFE2F2E9, "Explore Earth", this::showEarth);
    }

    private void showMath() {
        screen("Learn");
        intro("Math Kingdom 🔢", "Solve the puzzle and collect your stars.");
        if (mathIndex >= mathQuestions.length) mathIndex = 0;
        TextView progress = label("CHALLENGE  " + (mathIndex + 1) + " OF " + mathQuestions.length, 11, PURPLE, true);
        content.addView(progress);
        ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(mathQuestions.length);
        bar.setProgress(mathIndex);
        bar.setProgressTintList(android.content.res.ColorStateList.valueOf(PURPLE));
        bar.setProgressBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE6E3F2));
        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(-1, dp(7));
        pp.topMargin = dp(8);
        pp.bottomMargin = dp(22);
        content.addView(bar, pp);
        infoCard(mathQuestions[mathIndex], "Choose the correct answer.", 0xFFFFEBCB);
        final int current = mathIndex;
        boolean already = prefs.getBoolean("reward_math_" + current, false);
        if (already) {
            infoCard("Puzzle completed ✓", "You have already earned the star reward for this puzzle.", 0xFFE8F4DE);
        } else {
            for (int i = 0; i < 3; i++) {
                final int pick = i;
                button(mathOptions[current][i], false, () -> {
                    if (pick == mathAnswers[current]) {
                        rewardOnce("math_" + current, 5);
                        new AlertDialog.Builder(this).setTitle("Brilliant thinking! ⭐")
                            .setMessage("That's right! " + mathHints[current] + "\n\nYou earned 5 stars.")
                            .setPositiveButton("Continue", (d, w) -> showMath()).show();
                    } else {
                        new AlertDialog.Builder(this).setTitle("Good try!")
                            .setMessage("Have another go. Hint: " + mathHints[current])
                            .setPositiveButton("Try again", null).show();
                    }
                });
            }
        }
        button(current == mathQuestions.length - 1 ? "Finish maths" : "Next puzzle  →", true, () -> {
            mathIndex++;
            if (mathIndex >= mathQuestions.length) {
                mathIndex = 0;
                new AlertDialog.Builder(this).setTitle("Math mission complete!")
                    .setMessage("You reached the end of this maths set. Keep practicing to master every puzzle.")
                    .setPositiveButton("Great!", (d, w) -> showLearn()).show();
            } else showMath();
        });
        button("Back to learning worlds", false, this::showLearn);
    }

    private void showScience() {
        screen("Learn");
        intro("Science Galaxy 🔬", "Explore a topic, then test what you discovered.");
        infoCard("🌱 Plant power", "Roots take in water. Leaves capture light. Plants use light energy to make food in a process called photosynthesis.", 0xFFDFF3FF);
        infoCard("🌍 Earth in motion", "Earth spins on its axis once about every 24 hours. This rotation gives us day and night. Earth also travels around the Sun.", 0xFFECE9FF);
        infoCard("🧲 Forces around us", "A magnet can attract some metals, such as iron. Magnets have two poles, called north and south.", 0xFFE2F2E9);
        infoCard("💧 The water cycle", "Sunlight warms water and some evaporates into vapour. Cooling vapour condenses into droplets in clouds. Water returns as precipitation.", 0xFFFFEBCB);
        infoCard("🐬 Animal families", "Mammals breathe air and feed their babies milk. Dolphins are mammals even though they live in water.", 0xFFFFE6F0);
        smallHeading("Galaxy quiz · " + (scienceIndex + 1) + " of " + scienceQuestions.length);
        infoCard(scienceQuestions[scienceIndex], "Choose the best answer. Read the explanation after you answer.", WHITE);
        final int q = scienceIndex;
        if (prefs.getBoolean("reward_science_" + q, false)) {
            infoCard("Mission complete ✓", "You have already earned the reward for this question.", 0xFFE8F4DE);
        } else {
            for (int i = 0; i < 3; i++) {
                final int pick = i;
                button(scienceOptions[q][i], false, () -> scienceAnswer(pick == scienceAnswers[q], q));
            }
        }
        button(q == scienceQuestions.length - 1 ? "Finish science quiz" : "Next question  →", true, () -> {
            scienceIndex = (scienceIndex + 1) % scienceQuestions.length;
            showScience();
        });
    }

    private void scienceAnswer(boolean correct, int question) {
        if (correct) {
            rewardOnce("science_" + question, 5);
            new AlertDialog.Builder(this).setTitle("Science star earned! 🌟")
                .setMessage("Correct! " + scienceHints[question] + "\n\nYou earned 5 stars.")
                .setPositiveButton("Continue", (d, w) -> showScience()).show();
        } else {
            new AlertDialog.Builder(this).setTitle("Good experiment!")
                .setMessage("Try once more. Hint: " + scienceHints[question])
                .setPositiveButton("Try again", null).show();
        }
    }

    private void showStory() {
        screen("Learn");
        intro("Story Forest 📚", "Find a quiet spot and let your imagination grow.");
        TextView number = label("STORY " + (storyIndex + 1) + " OF " + storyTitles.length, 11, PURPLE, true);
        content.addView(number);
        infoCard(storyTitles[storyIndex], storyTexts[storyIndex], 0xFFE8F4DE);
        button("I finished reading  ·  +5 stars", true, () -> {
            rewardOnce("story_" + storyIndex, 5);
            if (storyIndex < storyTitles.length - 1) {
                storyIndex++;
                showStory();
            } else {
                new AlertDialog.Builder(this).setTitle("Story explorer! 📚")
                    .setMessage("You have read both stories. Wonderful work!")
                    .setPositiveButton("Done", (d, w) -> { storyIndex = 0; showLearn(); }).show();
            }
        });
        button("Back to learning worlds", false, this::showLearn);
    }

    private void showRobot() {
        screen("Learn");
        intro("Robot City 🤖", "A robot follows instructions one step at a time.");
        infoCard("Your mission", "Robo is standing beside a path. First move to the flower, then pick it up. Which instruction order makes sense?", 0xFFE8E5FF);
        button("1. Move forward   2. Pick flower", false, () -> {
            rewardOnce("robot_sequence", 10);
            new AlertDialog.Builder(this).setTitle("Mission complete! 🤖")
                .setMessage("You placed the instructions in a useful order. Robo found the flower. +10 stars!")
                .setPositiveButton("Nice!", (d, w) -> showLearn()).show();
        });
        button("1. Pick flower   2. Move forward", false, () ->
            new AlertDialog.Builder(this).setTitle("Try the order again")
                .setMessage("Robo needs to reach the flower before picking it up.")
                .setPositiveButton("Try again", null).show());
        button("Back to learning worlds", true, this::showLearn);
    }

    private void showEarth() {
        screen("Learn");
        intro("Explorer Earth 🌍", "Discover habitats and practise caring for our shared home.");
        infoCard("🌳 Forest habitats", "Forests provide food and shelter for countless living things. Trees also absorb carbon dioxide and release oxygen as they grow.", 0xFFE2F2E9);
        infoCard("🌊 Ocean life", "Oceans are home to coral reefs, fish, whales and tiny plankton. Plastic litter can harm animals, so keeping waste out of drains and waterways matters.", 0xFFDFF3FF);
        infoCard("♻️ The 3 Rs", "Reduce what you use, reuse items when safe, and recycle materials accepted by your local collection. Repairing and sharing can help too.", 0xFFFFEBCB);
        infoCard("🐝 Pollinators", "Bees, butterflies and some birds move pollen between flowers. This helps many plants make seeds and fruit.", 0xFFFFE6F0);
        String[] questions = {
            "Which action helps keep a river clean?",
            "What do bees help many flowers do?",
            "Which is a good way to reduce waste?",
            "Where do coral reefs grow?"
        };
        String[][] options = {
            {"Throw litter in a bin", "Drop wrappers in a drain", "Pour paint into water"},
            {"Make seeds by moving pollen", "Turn into leaves", "Make sunlight"},
            {"Use a refillable bottle", "Take a new bottle every hour", "Throw away reusable bags"},
            {"In warm, shallow ocean water", "On mountain peaks", "Deep inside deserts"}
        };
        int[] answers = {0, 0, 0, 0};
        String[] explanations = {
            "Litter belongs in a proper bin. Ask an adult about safe local clean-up activities.",
            "Pollinators move pollen between flowers, helping many plants make seeds and fruit.",
            "A refillable bottle can be used many times, so fewer single-use bottles are needed.",
            "Many coral reefs grow in warm, shallow ocean water where sunlight reaches them."
        };
        int q = earthIndex % questions.length;
        smallHeading("Planet protector quiz · " + (q + 1) + " of " + questions.length);
        infoCard(questions[q], "Choose the kindest answer for our planet.", WHITE);
        if (prefs.getBoolean("reward_earth_" + q, false)) {
            infoCard("Question completed ✓", "You have already earned this planet-protector star.", 0xFFE8F4DE);
        } else {
            for (int i = 0; i < 3; i++) {
                final int pick = i;
                button(options[q][i], false, () -> {
                    if (pick == answers[q]) {
                        rewardOnce("earth_" + q, 5);
                        new AlertDialog.Builder(this).setTitle("Planet protector! 🌍")
                            .setMessage(explanations[q] + "\\n\\nYou earned 5 stars.")
                            .setPositiveButton("Continue", (d, w) -> showEarth()).show();
                    } else {
                        new AlertDialog.Builder(this).setTitle("Try another idea")
                            .setMessage("Think about what keeps habitats healthy.")
                            .setPositiveButton("Try again", null).show();
                    }
                });
            }
        }
        button("Next Earth question  →", true, () -> {
            earthIndex = (earthIndex + 1) % questions.length;
            showEarth();
        });
        button("Back to learning worlds", false, this::showLearn);
    }

    private void showWorld() {
        screen("Home");
        intro("Your growing island 🏝️", "Every new skill helps your island grow.");
        int[] goals = {10, 30, 60, 100};
        String[] names = {"Cozy home", "Flower garden", "Robot buddy", "New island"};
        String[] icons = {"🏡", "🌷", "🤖", "🏝️"};
        for (int i = 0; i < goals.length; i++) {
            boolean unlocked = stars >= goals[i];
            int remaining = Math.max(0, goals[i] - stars);
            infoCard(icons[i] + "  " + names[i] + (unlocked ? "  ✓ UNLOCKED" : ""),
                unlocked ? "This reward is yours. Keep exploring!" : "Collect " + remaining + " more stars to unlock it. (" + stars + "/" + goals[i] + ")", 
                unlocked ? 0xFFE2F2E9 : WHITE);
        }
        button("Find another mission", true, this::showLearn);
    }

    private void showCreate() {
        screen("Create");
        intro("Creator Studio 🎨", "Your ideas belong on the page. Draw anything!");
        drawingCanvas = new DrawingCanvas(this);
        drawingCanvas.setBackground(shape(WHITE, 18));
        LinearLayout.LayoutParams canvasParams = new LinearLayout.LayoutParams(-1, dp(330));
        canvasParams.bottomMargin = dp(12);
        content.addView(drawingCanvas, canvasParams);

        LinearLayout palette = new LinearLayout(this);
        palette.setGravity(Gravity.CENTER);
        int[] colors = {0xFF6256D9, 0xFFFF6B80, 0xFF27A88A, 0xFFFFB547, 0xFF26324A, 0xFF2788D8};
        for (int color : colors) {
            View swatch = new View(this);
            swatch.setBackground(shape(color, 20));
            LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(dp(34), dp(34));
            sp.setMargins(dp(5), dp(3), dp(5), dp(12));
            palette.addView(swatch, sp);
            swatch.setOnClickListener(v -> drawingCanvas.setColor(color));
        }
        content.addView(palette);
        button("Save my drawing", true, this::saveDrawing);
        button("Clear canvas", false, () -> drawingCanvas.clear());
        button("Back home", false, this::showHome);
    }

    private void saveDrawing() {
        if (drawingCanvas == null || drawingCanvas.getWidth() <= 0) return;
        try {
            Bitmap bitmap = Bitmap.createBitmap(drawingCanvas.getWidth(), drawingCanvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawingCanvas.draw(canvas);
            File file = new File(getFilesDir(), "kidverse-drawing.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            bitmap.recycle();
            Toast.makeText(this, "Drawing saved on this device!", Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Toast.makeText(this, "Could not save the drawing. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private class DrawingCanvas extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private Bitmap layer;
        private Canvas layerCanvas;
        private int color = PURPLE;
        private float lastX, lastY;

        DrawingCanvas(Context context) {
            super(context);
            paint.setColor(color);
            paint.setStrokeWidth(dp(5));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override protected void onSizeChanged(int w, int h, int oldW, int oldH) {
            if (w <= 0 || h <= 0) return;
            layer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            layerCanvas = new Canvas(layer);
            layerCanvas.drawColor(WHITE);
        }

        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (layer != null) canvas.drawBitmap(layer, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX(), y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                path.moveTo(x, y);
                lastX = x; lastY = y;
                invalidate();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                path.quadTo(lastX, lastY, (x + lastX) / 2f, (y + lastY) / 2f);
                lastX = x; lastY = y;
                invalidate();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                path.lineTo(x, y);
                if (layerCanvas != null) layerCanvas.drawPath(path, paint);
                path.reset();
                invalidate();
                return true;
            }
            return true;
        }

        void setColor(int value) {
            color = value;
            paint.setColor(value);
        }

        void clear() {
            if (layerCanvas != null) layerCanvas.drawColor(WHITE);
            path.reset();
            invalidate();
        }
    }

    private void showParentGate() {
        screen("Parent");
        intro("Parent zone 🔒", "This area is intended for a parent or guardian.");
        infoCard("Adult check", "Please ask a grown-up to answer the question before opening progress settings.", 0xFFECE9FF);
        button("Continue to parent check", true, () -> {
            EditText answer = new EditText(this);
            answer.setInputType(InputType.TYPE_CLASS_NUMBER);
            answer.setSingleLine(true);
            new AlertDialog.Builder(this).setTitle("Parent check")
                .setMessage("What is 7 × 3?")
                .setView(answer)
                .setPositiveButton("Continue", (dialog, which) -> {
                    if ("21".equals(answer.getText().toString().trim())) showParent();
                    else Toast.makeText(this, "Please ask a parent or guardian to help.", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancel", null).show();
        });
    }

    private void showParent() {
        screen("Parent");
        intro("Parent dashboard", "A quick look at learning on this device.");
        statRow();
        space(12);
        infoCard("Privacy", "KIDVERSE stores progress and drawings locally on this device. Internet is used only to check app releases or download a learning pack. The app does not use accounts, location tracking, advertising or analytics.", 0xFFE2F2E9);
        infoCard("Completed activities", String.valueOf(prefs.getInt("completed", 0)) + " reward milestones recorded.", WHITE);
        button("Check for app updates", false, () -> UpdateManager.checkForUpdate(this, true));
        button("Install downloaded update", false, () -> UpdateManager.installPending(this));
        button("Reset all progress", false, () -> new AlertDialog.Builder(this)
            .setTitle("Reset KIDVERSE progress?")
            .setMessage("This removes stars and activity rewards from this device. This cannot be undone.")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Reset", (d, w) -> {
                prefs.edit().clear().apply();
                stars = 0;
                mathIndex = 0;
                storyIndex = 0;
                showParent();
            }).show());
        button("Back home", true, this::showHome);
    }
}
