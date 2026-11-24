package com.kidverse.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.view.Gravity;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import org.json.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class LibraryActivity extends Activity {
    private final ArrayList<JSONObject> topics = new ArrayList<>();
    private LinearLayout list;
    private EditText search;
    private boolean hindi = false;
    private int ink = 0xFF202044, purple = 0xFF6256D9, muted = 0xFF77758F;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        loadTopics();
        build();
    }

    private GradientDrawable bg(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(28); return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        t.setTypeface(Typeface.create("sans-serif-rounded", bold ? 1 : 0));
        return t;
    }

    private void build() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1); root.setPadding(18, 18, 18, 12); root.setBackgroundColor(0xFFF7F6FC);
        setContentView(root);
        TextView title = text("Knowledge Library", 25, ink, true);
        root.addView(title);
        TextView subtitle = text("Search, explore and learn at your own pace.", 13, muted, false);
        root.addView(subtitle);
        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(0); controls.setPadding(0, 12, 0, 8);
        Button language = new Button(this); language.setText("हिन्दी / English");
        language.setOnClickListener(v -> { hindi = !hindi; language.setText(hindi ? "हिन्दी selected" : "English selected"); render(search.getText().toString()); });
        Button download = new Button(this); download.setText("Download pack");
        download.setOnClickListener(v -> downloadPack(download));
        controls.addView(language, new LinearLayout.LayoutParams(0, -2, 1));
        controls.addView(download, new LinearLayout.LayoutParams(0, -2, 1));
        root.addView(controls);
        search = new EditText(this);
        search.setSingleLine(true); search.setTextSize(15); search.setHint(hindi ? "विषय खोजें..." : "Search any subject or topic...");
        search.setPadding(14, 8, 14, 8); search.setBackground(bg(Color.WHITE));
        root.addView(search, new LinearLayout.LayoutParams(-1, -2));
        Button practice = new Button(this); practice.setText("🎯 Practice generator — random question");
        practice.setOnClickListener(v -> practiceRandom());
        root.addView(practice, new LinearLayout.LayoutParams(-1, -2));
        ScrollView scroll = new ScrollView(this);
        list = new LinearLayout(this); list.setOrientation(1); list.setPadding(0, 10, 0, 20);
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,int st,int count,int after){}
            public void onTextChanged(CharSequence s,int start,int before,int count){render(s.toString());}
            public void afterTextChanged(Editable e){}
        });
        render("");
    }

    private void loadTopics() {
        try (InputStream in = getAssets().open("library.json")) { readPack(in); } catch (Exception ignored) {}
        File pack = new File(getFilesDir(), "knowledge-pack-01.json");
        if (pack.exists()) try (InputStream in = new FileInputStream(pack)) { readPack(in); } catch (Exception ignored) {}
    }

    private void readPack(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096]; int n;
        while ((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
        JSONArray array = new JSONObject(out.toString("UTF-8")).optJSONArray("topics");
        if (array == null) return;
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            String id = item.optString("id");
            int existingIndex = -1;
            for (int j = 0; j < topics.size(); j++) {
                if (id.equals(topics.get(j).optString("id"))) { existingIndex = j; break; }
            }
            if (existingIndex >= 0) topics.set(existingIndex, item);
            else topics.add(item);
        }
    }

    private void render(String query) {
        if (list == null) return;
        list.removeAllViews();
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        int shown = 0;
        for (JSONObject item : topics) {
            String title = item.optString(hindi ? "titleHi" : "title", item.optString("title"));
            String subject = item.optString("subject");
            String hay = item.toString().toLowerCase(Locale.ROOT);
            if (!hay.contains(q)) continue;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(1); card.setPadding(15, 13, 15, 13); card.setBackground(bg(Color.WHITE));
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.bottomMargin = 9;
            list.addView(card, p);
            card.addView(text(subject.toUpperCase(Locale.ROOT), 10, purple, true));
            TextView name = text(title, 17, ink, true); name.setPadding(0, 4, 0, 3); card.addView(name);
            String teaser = localized(item, "intro", "introHi");
            if (teaser.isEmpty()) teaser = localized(item, "simple", "simpleHi");
            TextView preview = text(teaser, 13, 0xFF5F5B75, false);
            preview.setMaxLines(3);
            preview.setEllipsize(android.text.TextUtils.TruncateAt.END);
            preview.setPadding(0, 5, 0, 6);
            card.addView(preview);
            String meta = item.optString("level", hindi ? "ज्ञान पाठ" : "Knowledge lesson") +
                "  ·  " + item.optString("minutes", "5") + (hindi ? " मिनट" : " min");
            card.addView(text(meta, 11, muted, false));
            card.addView(text(hindi ? "पूरा पाठ खोलें  →" : "Open full lesson  →", 12, purple, true));
            card.setOnClickListener(v -> openTopic(item));
            shown++;
        }
        if (shown == 0) list.addView(text(hindi ? "कोई विषय नहीं मिला। दूसरा शब्द खोजें।" : "No topics found. Try another search.", 14, muted, false));
        TextView count = text((hindi ? "कुल विषय: " : "Topics available: ") + topics.size(), 12, muted, false);
        list.addView(count);
    }

    private String localized(JSONObject item, String englishKey, String hindiKey) {
        String value = item.optString(hindi ? hindiKey : englishKey, "");
        if (value.isEmpty()) value = item.optString(englishKey, "");
        return value;
    }

    private TextView lessonText(String value, int size, int color, boolean bold) {
        TextView t = text(value, size, color, bold);
        t.setLineSpacing(5f, 1.08f);
        return t;
    }

    private void lessonBlock(LinearLayout parent, String heading, String body, String example) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(1);
        card.setPadding(16, 14, 16, 14);
        card.setBackground(bg(Color.WHITE));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = 10;
        parent.addView(card, p);
        TextView h = lessonText(heading, 17, ink, true);
        card.addView(h);
        TextView b = lessonText(body, 15, 0xFF4F4D63, false);
        b.setPadding(0, 8, 0, 0);
        card.addView(b);
        if (example != null && !example.trim().isEmpty()) {
            TextView e = lessonText((hindi ? "उदाहरण: " : "Example: ") + example, 14, purple, false);
            e.setPadding(0, 10, 0, 0);
            card.addView(e);
        }
    }

    private void openTopic(JSONObject item) {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(1);
        page.setPadding(18, 12, 18, 10);
        page.setBackgroundColor(0xFFF7F6FC);
        setContentView(page);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        Button back = new Button(this);
        back.setText(hindi ? "← वापस" : "← Back");
        back.setOnClickListener(v -> { build(); render(""); });
        top.addView(back);
        TextView label = lessonText(hindi ? "पाठ" : "LESSON", 11, purple, true);
        label.setPadding(12, 0, 0, 0);
        top.addView(label);
        page.addView(top, new LinearLayout.LayoutParams(-1, -2));

        ScrollView scroll = new ScrollView(this);
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(1);
        body.setPadding(0, 12, 0, 22);
        scroll.addView(body);
        page.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        String title = localized(item, "title", "titleHi");
        String subject = item.optString("subject", "Learning");
        body.addView(lessonText(subject.toUpperCase(Locale.ROOT) + "  ·  " +
            item.optString("level", hindi ? "ज्ञान पाठ" : "Knowledge lesson") +
            "  ·  " + item.optString("minutes", "6") + (hindi ? " मिनट" : " min"), 11, purple, true));
        TextView heading = lessonText(title, 25, ink, true);
        heading.setPadding(0, 7, 0, 10);
        body.addView(heading);

        String intro = localized(item, "intro", "introHi");
        if (intro.isEmpty()) intro = localized(item, "simple", "simpleHi");
        lessonBlock(body, hindi ? "इस पाठ में क्या सीखेंगे" : "What you will learn", intro, null);

        JSONArray sections = item.optJSONArray("sections");
        if (sections != null && sections.length() > 0) {
            TextView sectionTitle = lessonText(hindi ? "समझें, चरण-दर-चरण" : "Learn step by step", 19, ink, true);
            sectionTitle.setPadding(2, 5, 0, 10);
            body.addView(sectionTitle);
            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.optJSONObject(i);
                if (section == null) continue;
                String sh = localized(section, "heading", "headingHi");
                String sb = localized(section, "body", "bodyHi");
                String se = localized(section, "example", "exampleHi");
                lessonBlock(body, sh, sb, se);
            }
        } else {
            lessonBlock(body, hindi ? "और जानें" : "Go deeper",
                localized(item, "deep", "deepHi"), null);
        }

        JSONArray terms = item.optJSONArray("terms");
        if (terms != null && terms.length() > 0) {
            TextView termsTitle = lessonText(hindi ? "मुख्य शब्द" : "Key words", 19, ink, true);
            termsTitle.setPadding(2, 8, 0, 10);
            body.addView(termsTitle);
            for (int i = 0; i < terms.length(); i++) {
                JSONObject term = terms.optJSONObject(i);
                if (term == null) continue;
                String word = localized(term, "word", "wordHi");
                String meaning = localized(term, "meaning", "meaningHi");
                lessonBlock(body, word, meaning, null);
            }
        }

        String activity = localized(item, "activity", "activityHi");
        if (!activity.isEmpty()) {
            lessonBlock(body, hindi ? "करके देखो" : "Try it yourself", activity, null);
        }

        JSONArray questions = item.optJSONArray("practice");
        if (questions != null && questions.length() > 0) {
            TextView quizTitle = lessonText(hindi ? "अपनी समझ जाँचें" : "Check your understanding", 20, ink, true);
            quizTitle.setPadding(2, 10, 0, 10);
            body.addView(quizTitle);
            for (int i = 0; i < questions.length(); i++) {
                final JSONObject q = questions.optJSONObject(i);
                if (q == null) continue;
                final JSONArray choices = q.optJSONArray(hindi ? "optionsHi" : "options");
                final int correct = q.optInt("correct", -1);
                String question = localized(q, "question", "questionHi");
                lessonBlock(body, (hindi ? "प्रश्न " : "Question ") + (i + 1), question, null);
                if (choices != null) {
                    for (int j = 0; j < choices.length(); j++) {
                        final int selected = j;
                        String option = choices.optString(j);
                        Button optionButton = new Button(this);
                        optionButton.setText(option);
                        optionButton.setAllCaps(false);
                        LinearLayout.LayoutParams op = new LinearLayout.LayoutParams(-1, -2);
                        op.bottomMargin = 5;
                        body.addView(optionButton, op);
                        optionButton.setOnClickListener(v -> {
                            boolean right = selected == correct;
                            String explanation = localized(q, "explanation", "explanationHi");
                            String correctAnswer = choices != null && correct >= 0 && correct < choices.length()
                                ? choices.optString(correct) : "";
                            String message = (right ? (hindi ? "सही जवाब! 🌟\n\n" : "Correct! 🌟\n\n") :
                                (hindi ? "अच्छी कोशिश। सही उत्तर: " : "Good try. Correct answer: ") + correctAnswer + "\n\n") + explanation;
                            new AlertDialog.Builder(this)
                                .setTitle(right ? (hindi ? "शाबाश!" : "Well done!") : (hindi ? "फिर प्रयास करें" : "Try again"))
                                .setMessage(message)
                                .setPositiveButton("OK", null).show();
                        });
                    }
                }
            }
        } else {
            Button practiceButton = new Button(this);
            practiceButton.setText(hindi ? "इस विषय का अभ्यास करें" : "Practise this topic");
            practiceButton.setOnClickListener(v -> practice(item));
            body.addView(practiceButton);
        }

        Button backBottom = new Button(this);
        backBottom.setText(hindi ? "पुस्तकालय में वापस जाएँ" : "Back to library");
        backBottom.setOnClickListener(v -> { build(); render(""); });
        body.addView(backBottom);
    }

    private void practiceRandom() {
        ArrayList<JSONObject> questionPool = new ArrayList<>();
        for (JSONObject topic : topics) {
            JSONArray questions = topic.optJSONArray("practice");
            if (questions == null) continue;
            for (int i = 0; i < questions.length(); i++) {
                JSONObject q = questions.optJSONObject(i);
                if (q != null && q.optJSONArray("options") != null) questionPool.add(q);
            }
        }
        if (questionPool.isEmpty()) {
            if (!topics.isEmpty()) practice(topics.get(new Random().nextInt(topics.size())));
            return;
        }
        JSONObject q = questionPool.get(new Random().nextInt(questionPool.size()));
        String question = localized(q, "question", "questionHi");
        JSONArray choices = q.optJSONArray(hindi ? "optionsHi" : "options");
        if (choices == null) choices = q.optJSONArray("options");
        int correct = q.optInt("correct", -1);
        String[] labels = new String[choices == null ? 0 : choices.length()];
        for (int i = 0; i < labels.length; i++) labels[i] = choices.optString(i);
        new AlertDialog.Builder(this)
            .setTitle(hindi ? "त्वरित अभ्यास" : "Quick practice")
            .setMessage(question)
            .setItems(labels, (dialog, selected) -> {
                String answer = correct >= 0 && correct < labels.length ? labels[correct] : "";
                String explanation = localized(q, "explanation", "explanationHi");
                boolean right = selected == correct;
                String message = (right ? (hindi ? "सही जवाब! 🌟" : "Correct! 🌟") :
                    (hindi ? "सही उत्तर: " : "Correct answer: ") + answer) + "\\n\\n" + explanation;
                new AlertDialog.Builder(this)
                    .setTitle(right ? (hindi ? "शाबाश!" : "Well done!") : (hindi ? "फिर प्रयास करें" : "Try again"))
                    .setMessage(message)
                    .setPositiveButton("OK", null).show();
            })
            .setNegativeButton(hindi ? "बाद में" : "Later", null)
            .show();
    }

    private void practice(JSONObject item) {
        String question = item.optString(hindi ? "questionHi" : "question", item.optString("question"));
        String answer = item.optString(hindi ? "answerHi" : "answer", item.optString("answer"));
        new AlertDialog.Builder(this).setTitle(hindi ? "अभ्यास प्रश्न" : "Practice question")
            .setMessage(question).setNegativeButton(hindi ? "बाद में" : "Later", null)
            .setPositiveButton(hindi ? "उत्तर दिखाएँ" : "Reveal answer", (d,w) ->
                new AlertDialog.Builder(this).setTitle(hindi ? "उत्तर" : "Answer").setMessage(answer).setPositiveButton("OK", null).show()).show();
    }

    private void downloadPack(Button button) {
        button.setEnabled(false);
        new Thread(() -> {
            File temp = new File(getFilesDir(), "knowledge-pack-01.tmp");
            try {
                URL url = new URL("https://raw.githubusercontent.com/rautshivamxyz-beep/KIDVERSE/main/content/packs/knowledge-pack-01.json");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setConnectTimeout(10000); c.setReadTimeout(15000); c.setRequestProperty("User-Agent", "KIDVERSE-Android");
                if (c.getResponseCode() != 200) throw new IOException("Download failed");
                try (InputStream in = c.getInputStream(); FileOutputStream out = new FileOutputStream(temp)) {
                    byte[] b = new byte[4096]; int n; while ((n = in.read(b)) > 0) out.write(b, 0, n);
                } finally { c.disconnect(); }
                try (InputStream verify = new FileInputStream(temp)) {
                    JSONObject json = new JSONObject(new String(readAll(verify), "UTF-8"));
                    if (json.optJSONArray("topics") == null) throw new IOException("Invalid pack");
                }
                File target = new File(getFilesDir(), "knowledge-pack-01.json");
                if (!temp.renameTo(target)) throw new IOException("Could not save pack");
                runOnUiThread(() -> { loadTopics(); render(search.getText().toString()); button.setEnabled(true); Toast.makeText(this, "Knowledge pack saved for offline learning.", Toast.LENGTH_LONG).show(); });
            } catch (Exception e) {
                temp.delete();
                runOnUiThread(() -> { button.setEnabled(true); Toast.makeText(this, "Could not download pack. Try again when online.", Toast.LENGTH_LONG).show(); });
            }
        }).start();
    }

    private byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); byte[] b = new byte[4096]; int n;
        while ((n = in.read(b)) > 0) out.write(b, 0, n); return out.toByteArray();
    }
}
