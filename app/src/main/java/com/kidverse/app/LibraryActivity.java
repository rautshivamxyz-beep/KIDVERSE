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
            boolean exists = false;
            for (JSONObject old : topics) if (id.equals(old.optString("id"))) { exists = true; break; }
            if (!exists) topics.add(item);
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
            String hay = (item.optString("title") + " " + item.optString("titleHi") + " " + subject + " " + item.optString("simple")).toLowerCase(Locale.ROOT);
            if (!hay.contains(q)) continue;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(1); card.setPadding(15, 13, 15, 13); card.setBackground(bg(Color.WHITE));
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2); p.bottomMargin = 9;
            list.addView(card, p);
            card.addView(text(subject.toUpperCase(Locale.ROOT), 10, purple, true));
            TextView name = text(title, 17, ink, true); name.setPadding(0, 4, 0, 3); card.addView(name);
            card.addView(text(hindi ? "पढ़ने के लिए खोलें  →" : "Open lesson  →", 12, muted, false));
            card.setOnClickListener(v -> openTopic(item));
            shown++;
        }
        if (shown == 0) list.addView(text(hindi ? "कोई विषय नहीं मिला। दूसरा शब्द खोजें।" : "No topics found. Try another search.", 14, muted, false));
        TextView count = text((hindi ? "कुल विषय: " : "Topics available: ") + topics.size(), 12, muted, false);
        list.addView(count);
    }

    private void openTopic(JSONObject item) {
        String title = item.optString(hindi ? "titleHi" : "title", item.optString("title"));
        String simple = item.optString(hindi ? "simpleHi" : "simple", item.optString("simple"));
        String deep = item.optString(hindi ? "deepHi" : "deep", item.optString("deep"));
        String[] choices = hindi ? new String[]{"आसान व्याख्या पढ़ें", "और गहराई से समझें", "इस विषय पर अभ्यास करें"} :
            new String[]{"Simple explanation", "Explain more deeply", "Practice this topic"};
        new AlertDialog.Builder(this).setTitle(title).setItems(choices, (d, which) -> {
            if (which == 0) new AlertDialog.Builder(this).setTitle(title).setMessage(simple).setPositiveButton("OK", null).show();
            else if (which == 1) new AlertDialog.Builder(this).setTitle(title).setMessage(deep).setPositiveButton("Got it", null).show();
            else practice(item);
        }).setNegativeButton(hindi ? "बंद करें" : "Close", null).show();
    }

    private void practiceRandom() {
        if (topics.isEmpty()) return;
        practice(topics.get(new Random().nextInt(topics.size())));
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
