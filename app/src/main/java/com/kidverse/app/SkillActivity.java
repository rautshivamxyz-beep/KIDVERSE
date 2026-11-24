package com.kidverse.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SkillActivity extends Activity {
    private static final int INK = 0xFF202044;
    private static final int MUTED = 0xFF77758F;
    private static final int PURPLE = 0xFF6256D9;
    private static final int BG = 0xFFF7F6FC;
    private static final int WHITE = 0xFFFFFFFF;

    private SharedPreferences prefs;
    private LinearLayout root;
    private LinearLayout content;
    private boolean hindi = false;
    private boolean answeredCorrect = false;
    private boolean[] practiceChecks;

    private static class Skill {
        final String id, icon, title, titleHi, area, areaHi, goal, goalHi;
        final String scenario, scenarioHi, options[], optionsHi[];
        final int answer;
        final String feedback, feedbackHi, mission, missionHi;
        final String steps[], stepsHi[], safety, safetyHi, reflection, reflectionHi;

        Skill(String id, String icon, String title, String titleHi, String area, String areaHi,
              String goal, String goalHi, String scenario, String scenarioHi,
              String[] options, String[] optionsHi, int answer,
              String feedback, String feedbackHi, String mission, String missionHi,
              String[] steps, String[] stepsHi, String safety, String safetyHi,
              String reflection, String reflectionHi) {
            this.id=id; this.icon=icon; this.title=title; this.titleHi=titleHi;
            this.area=area; this.areaHi=areaHi; this.goal=goal; this.goalHi=goalHi;
            this.scenario=scenario; this.scenarioHi=scenarioHi; this.options=options;
            this.optionsHi=optionsHi; this.answer=answer; this.feedback=feedback;
            this.feedbackHi=feedbackHi; this.mission=mission; this.missionHi=missionHi;
            this.steps=steps; this.stepsHi=stepsHi; this.safety=safety; this.safetyHi=safetyHi;
            this.reflection=reflection; this.reflectionHi=reflectionHi;
        }
    }

    private final Skill[] skills = {
        new Skill("daily", "🌞", "Daily independence", "रोज़मर्रा के काम",
            "Self-care and organisation", "अपना ध्यान और व्यवस्था",
            "Build small habits that make your day easier.", "छोटी आदतें अपनाओ जो दिन को आसान बनाएँ।",
            "It is a school morning. What is a helpful way to avoid a last-minute rush?",
            "सुबह स्कूल जाना है। आख़िरी समय की भागदौड़ से बचने का अच्छा तरीका क्या है?",
            new String[]{"Pack your school bag the night before", "Wait until the school bus arrives", "Leave important things scattered"},
            new String[]{"रात में ही स्कूल बैग तैयार करना", "स्कूल बस आने तक इंतज़ार करना", "ज़रूरी चीज़ें बिखरी छोड़ना"}, 0,
            "Planning ahead saves time. A simple checklist can help you remember what you need.",
            "पहले से तैयारी करने से समय बचता है। छोटी सूची चीज़ें याद रखने में मदद करती है।",
            "Make a simple morning or school-bag routine with a parent or guardian.",
            "माता-पिता या अभिभावक के साथ सुबह या स्कूल बैग की छोटी दिनचर्या बनाओ।",
            new String[]{"Choose a regular place for your school things.", "Pack books and supplies using your timetable.", "Practise putting used items back in their place."},
            new String[]{"स्कूल की चीज़ों के लिए एक जगह तय करो।", "समय-सारणी देखकर किताबें और सामान रखो।", "इस्तेमाल की चीज़ें वापस सही जगह रखना सीखो।"},
            "Ask an adult before handling medicines, cleaning products or appliances.",
            "दवा, सफ़ाई के सामान या बिजली के उपकरण छूने से पहले बड़े से पूछो।",
            "Which small habit would make tomorrow easier?",
            "कौन-सी छोटी आदत कल को आसान बनाएगी?"),

        new Skill("communication", "🗣️", "Communication and kindness", "बातचीत और दयालुता",
            "Listening and expressing ideas", "सुनना और अपने विचार बताना",
            "Listen carefully, speak clearly and ask for help when needed.",
            "ध्यान से सुनो, साफ़ बोलो और ज़रूरत पर मदद माँगो।",
            "A friend is telling you about a problem. What should you do first?",
            "तुम्हारा दोस्त अपनी परेशानी बता रहा है। सबसे पहले क्या करना चाहिए?",
            new String[]{"Listen without interrupting", "Laugh and change the subject", "Talk over your friend"},
            new String[]{"बीच में टोके बिना सुनना", "हँसकर बात बदल देना", "दोस्त की बात के ऊपर अपनी बात कहना"}, 0,
            "Listening shows respect. You can ask, “Would you like help or would you like me to listen?”",
            "ध्यान से सुनना सम्मान दिखाता है। तुम पूछ सकते हो, “क्या तुम्हें मदद चाहिए या मैं बस सुनूँ?”",
            "Practise a two-minute conversation with someone you trust: listen, then repeat the main idea.",
            "किसी भरोसेमंद व्यक्ति के साथ दो मिनट बात करो: पहले सुनो, फिर उसकी मुख्य बात दोहराओ।",
            new String[]{"Let the other person finish speaking.", "Ask one kind question.", "Share your idea using calm, respectful words."},
            new String[]{"दूसरे व्यक्ति को बात पूरी करने दो।", "एक अच्छा सवाल पूछो।", "शांत और सम्मानजनक शब्दों में अपनी बात कहो।"},
            "You never have to keep a scary secret. Tell a trusted adult if something feels unsafe.",
            "डरावनी बात को राज़ रखना ज़रूरी नहीं। असुरक्षित लगे तो भरोसेमंद बड़े को बताओ।",
            "What helped you feel heard during the conversation?",
            "बातचीत में तुम्हें कब लगा कि तुम्हारी बात सुनी गई?"),

        new Skill("problem", "🧩", "Problem-solving", "समस्या सुलझाना",
            "Reasoning and persistence", "सोच और कोशिश",
            "Break a big problem into smaller steps and test ideas.",
            "बड़ी समस्या को छोटे चरणों में बाँटो और उपाय आज़माओ।",
            "Your building blocks keep falling over. What is a sensible next step?",
            "तुम्हारे खिलौने के ब्लॉक बार-बार गिर रहे हैं। अब क्या करना ठीक होगा?",
            new String[]{"Make the base wider and try again", "Give up immediately", "Blame someone else"},
            new String[]{"नीचे का आधार चौड़ा करके फिर कोशिश करना", "तुरंत हार मान लेना", "किसी और को दोष देना"}, 0,
            "A wider base may make a structure steadier. Good problem-solvers observe, change one thing and test again.",
            "चौड़ा आधार ढाँचे को मज़बूत बना सकता है। अच्छे समस्या-सुलझाने वाले देखते हैं, एक बदलाव करते हैं और फिर जाँचते हैं।",
            "Build a small block tower or paper shape. Change one thing each time it falls.",
            "ब्लॉक या कागज़ से छोटा ढाँचा बनाओ। गिरने पर हर बार एक चीज़ बदलकर देखो।",
            new String[]{"Say what the problem is.", "Think of two possible ideas.", "Try one idea and notice what changes."},
            new String[]{"समस्या क्या है, यह बताओ।", "दो संभावित उपाय सोचो।", "एक उपाय आज़माओ और बदलाव देखो।"},
            "Use safe materials and ask an adult before using scissors or tools.",
            "सुरक्षित सामग्री उपयोग करो और कैंची या औज़ार लेने से पहले बड़े से पूछो।",
            "What did you change, and what happened?",
            "तुमने क्या बदला और फिर क्या हुआ?"),

        new Skill("money", "🪙", "Money sense", "पैसों की समझ",
            "Needs, wants and planning", "ज़रूरत, इच्छा और योजना",
            "Learn to make thoughtful choices with money.",
            "पैसों के बारे में सोच-समझकर चुनाव करना सीखो।",
            "You receive ₹20 as a gift. What is a thoughtful first step?",
            "तुम्हें उपहार में ₹20 मिलते हैं। समझदारी भरा पहला कदम क्या है?",
            new String[]{"Think about saving and choices with a trusted adult", "Spend it all without thinking", "Hide it and never talk about it"},
            new String[]{"भरोसेमंद बड़े के साथ बचत और विकल्पों पर सोचना", "बिना सोचे सब खर्च कर देना", "छिपा देना और कभी बात न करना"}, 0,
            "Planning helps you decide between needs and wants. Families may make different money choices.",
            "योजना बनाने से ज़रूरत और इच्छा में अंतर समझ आता है। हर परिवार के पैसों के फैसले अलग हो सकते हैं।",
            "With a parent or guardian, make three labelled jars or paper boxes: Save, Spend and Share. Use pretend coins if you prefer.",
            "माता-पिता या अभिभावक के साथ तीन डिब्बे बनाओ: बचत, खर्च और बाँटना। चाहो तो नकली सिक्के इस्तेमाल करो।",
            new String[]{"Name one thing you need and one thing you want.", "Choose a simple saving goal with an adult.", "Decide together how pretend or gift money could be divided."},
            new String[]{"एक ज़रूरत और एक इच्छा बताओ।", "बड़े के साथ बचत का छोटा लक्ष्य चुनो।", "मिलकर तय करो कि नकली या उपहार के पैसों को कैसे बाँटेंगे।"},
            "Never share a bank PIN, password, OTP or payment detail. Children should not make purchases alone.",
            "बैंक PIN, पासवर्ड, OTP या भुगतान की जानकारी कभी साझा न करो। बच्चे अकेले खरीदारी न करें।",
            "What would you like to save for, and why?",
            "तुम किस चीज़ के लिए बचत करना चाहोगे और क्यों?"),

        new Skill("digital", "🛡️", "Digital safety", "डिजिटल सुरक्षा",
            "Privacy and asking for help", "निजता और मदद माँगना",
            "Recognise unsafe requests and pause before clicking or sharing.",
            "असुरक्षित अनुरोध पहचानो और क्लिक या साझा करने से पहले रुको।",
            "A person in a game asks for your full name, home address or school. What should you do?",
            "किसी गेम में कोई तुम्हारा पूरा नाम, घर का पता या स्कूल पूछता है। तुम्हें क्या करना चाहिए?",
            new String[]{"Do not share; stop and tell a trusted adult", "Send the details to be friendly", "Ask them to keep it secret"},
            new String[]{"जानकारी न दो; रुककर भरोसेमंद बड़े को बताओ", "दोस्ती के लिए जानकारी भेज दो", "उन्हें इसे राज़ रखने को कहो"}, 0,
            "Personal details stay private. A trusted adult can help block or report unsafe behaviour.",
            "निजी जानकारी गुप्त रखो। भरोसेमंद बड़ा असुरक्षित व्यवहार को ब्लॉक या रिपोर्ट करने में मदद कर सकता है।",
            "Create a family digital-safety rule card with an adult: what to do before clicking, downloading or chatting.",
            "बड़े के साथ परिवार के डिजिटल-सुरक्षा नियम बनाओ: क्लिक, डाउनलोड या चैट से पहले क्या करना है।",
            new String[]{"Keep your address, school, phone number and passwords private.", "Do not meet online contacts in person.", "Pause and ask an adult before opening unknown links or downloads."},
            new String[]{"पता, स्कूल, फ़ोन नंबर और पासवर्ड निजी रखो।", "ऑनलाइन मिले व्यक्ति से अकेले मिलने मत जाओ।", "अनजान लिंक या डाउनलोड खोलने से पहले बड़े से पूछो।"},
            "If a message scares you or asks for a secret, close it and tell a trusted adult. You are not in trouble for asking.",
            "कोई संदेश डराए या राज़ माँगे तो उसे बंद करके भरोसेमंद बड़े को बताओ। मदद माँगने पर तुम्हें डाँटा नहीं जाना चाहिए।",
            "Who are two trusted adults you can talk to?",
            "वे कौन-से दो भरोसेमंद बड़े हैं जिनसे तुम बात कर सकते हो?"),

        new Skill("nature", "🌱", "Nature and citizenship", "प्रकृति और नागरिकता",
            "Care for shared spaces", "साझा जगहों की देखभाल",
            "Notice your surroundings and take small, safe actions.",
            "अपने आसपास ध्यान दो और छोटे, सुरक्षित काम करो।",
            "You see a wrapper on the classroom floor. What is a helpful response?",
            "तुम्हें कक्षा के फ़र्श पर एक रैपर दिखता है। क्या करना उपयोगी होगा?",
            new String[]{"Put it in the correct bin if safe and allowed", "Push it under a desk", "Throw it outside"},
            new String[]{"सुरक्षित और अनुमति होने पर सही डिब्बे में डालना", "डेस्क के नीचे छिपा देना", "बाहर फेंक देना"}, 0,
            "Shared spaces belong to everyone. Sorting waste correctly depends on the bins available locally.",
            "साझा जगह सबकी होती है। कचरे को सही तरह अलग करना वहाँ उपलब्ध डिब्बों पर निर्भर करता है।",
            "With an adult, observe one small area and find one safe way to care for it.",
            "बड़े के साथ किसी छोटी जगह को देखो और उसकी देखभाल का एक सुरक्षित तरीका खोजो।",
            new String[]{"Notice where waste and recycling bins are.", "Save water by turning off a tap after use.", "Observe a plant or bird without disturbing it."},
            new String[]{"कचरे और पुनर्चक्रण के डिब्बे कहाँ हैं, देखो।", "इस्तेमाल के बाद नल बंद करके पानी बचाओ।", "पौधे या पक्षी को बिना परेशान किए देखो।"},
            "Do not touch sharp waste, chemicals, unknown plants or stray animals. Ask an adult to handle hazards.",
            "नुकीले कचरे, रसायन, अनजान पौधों या आवारा जानवरों को मत छुओ। ख़तरे बड़े को सँभालने दो।",
            "What small action could help your home, school or neighbourhood?",
            "कौन-सा छोटा काम घर, स्कूल या मोहल्ले की मदद कर सकता है?"),

        new Skill("creative", "🛠️", "Creative making", "रचनात्मक निर्माण",
            "Design, build and improve", "सोचना, बनाना और सुधारना",
            "Turn an idea into a simple creation and improve it.",
            "विचार को छोटी रचना में बदलो और उसे बेहतर बनाओ।",
            "Your paper bridge cannot hold a toy. What should a maker do?",
            "तुम्हारा कागज़ का पुल खिलौना नहीं उठा पा रहा। एक निर्माता क्या करेगा?",
            new String[]{"Test a fold or support and try again", "Say making things is impossible", "Hide the bridge"},
            new String[]{"मोड़ या सहारा बदलकर फिर जाँचना", "कहना कि कुछ बन ही नहीं सकता", "पुल छिपा देना"}, 0,
            "Design improves through testing. A failed test gives useful information about what to change.",
            "जाँच से डिज़ाइन बेहतर होता है। असफल कोशिश भी बताती है कि क्या बदलना है।",
            "Make a paper bridge between two books. Test it with small, safe objects and improve the design.",
            "दो किताबों के बीच कागज़ का पुल बनाओ। छोटी, सुरक्षित चीज़ों से जाँचो और डिज़ाइन सुधारो।",
            new String[]{"Draw or describe your idea first.", "Build a simple version with safe reusable materials.", "Test it gently and make one improvement."},
            new String[]{"पहले अपने विचार का चित्र बनाओ या बताओ।", "सुरक्षित दोबारा उपयोग होने वाली सामग्री से सरल रूप बनाओ।", "धीरे से जाँचो और एक सुधार करो।"},
            "Ask an adult before using scissors, glue, batteries or any tool. Never use mains electricity.",
            "कैंची, गोंद, बैटरी या औज़ार लेने से पहले बड़े से पूछो। घर की बिजली से प्रयोग कभी न करो।",
            "What is one improvement you would make next time?",
            "अगली बार तुम कौन-सा एक सुधार करोगे?"),

        new Skill("feelings", "💛", "Feelings and wellbeing", "भावनाएँ और अच्छा महसूस करना",
            "Self-awareness and healthy routines", "खुद को समझना और अच्छी दिनचर्या",
            "Notice feelings and choose a safe, helpful next step.",
            "अपनी भावनाएँ पहचानो और सुरक्षित, मददगार अगला कदम चुनो।",
            "You feel frustrated because a task is difficult. What could help?",
            "कोई काम कठिन होने से तुम्हें निराशा हो रही है। क्या मदद कर सकता है?",
            new String[]{"Pause, take a few calm breaths and ask for help", "Throw things or hurt someone", "Pretend you never feel anything"},
            new String[]{"रुकना, धीरे साँस लेना और मदद माँगना", "चीज़ें फेंकना या किसी को चोट पहुँचाना", "ऐसा दिखाना कि कोई भावना होती ही नहीं"}, 0,
            "All feelings are allowed. A pause, calm breathing and talking to a trusted person can help.",
            "हर भावना स्वाभाविक है। थोड़ा रुकना, धीरे साँस लेना और भरोसेमंद व्यक्ति से बात करना मदद कर सकता है।",
            "Make a small calm-down plan with an adult: a quiet place, a breathing count or a person to talk to.",
            "बड़े के साथ शांत होने की छोटी योजना बनाओ: शांत जगह, साँस गिनना या बात करने वाला व्यक्ति।",
            new String[]{"Name the feeling without judging it.", "Try three slow, comfortable breaths.", "Tell a trusted person what would help."},
            new String[]{"बिना खुद को दोष दिए भावना का नाम बताओ।", "तीन बार धीरे और आराम से साँस लो।", "भरोसेमंद व्यक्ति को बताओ कि किससे मदद मिलेगी।"},
            "If you feel unsafe, very worried or hurt, tell a trusted adult. This activity is not medical care.",
            "असुरक्षित, बहुत चिंतित या आहत महसूस हो तो भरोसेमंद बड़े को बताओ। यह गतिविधि चिकित्सा सहायता नहीं है।",
            "What helps you feel calm, safe and supported?",
            "किससे तुम्हें शांत, सुरक्षित और सहारा मिला हुआ महसूस होता है?")
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("kidverse_skills", MODE_PRIVATE);
        hindi = prefs.getBoolean("hindi", false);
        showHome();
    }

    private int dp(float value) {
        return (int)(value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(color);
        t.setTypeface(Typeface.create("sans-serif-rounded", bold ? Typeface.BOLD : Typeface.NORMAL));
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setLineSpacing(dp(3), 1.05f);
        return t;
    }

    private void setup(String title) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        setContentView(root);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(18), dp(12), dp(18), dp(10));
        TextView heading = text("✦  " + title, 20, INK, true);
        top.addView(heading, new LinearLayout.LayoutParams(0, dp(42), 1));
        Button language = new Button(this);
        language.setText(hindi ? "English" : "हिन्दी");
        language.setAllCaps(false);
        language.setOnClickListener(v -> {
            hindi = !hindi;
            prefs.edit().putBoolean("hindi", hindi).apply();
            showHome();
        });
        top.addView(language);
        root.addView(top, new LinearLayout.LayoutParams(-1, -2));

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(6), dp(18), dp(24));
        scroll.addView(content, new ScrollView.LayoutParams(-1, -2));
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void addCard(String title, String body, int color) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(13), dp(15), dp(13));
        card.setBackground(shape(color, 18));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(10);
        content.addView(card, p);
        card.addView(text(title, 16, INK, true));
        TextView detail = text(body, 14, 0xFF55536B, false);
        detail.setPadding(0, dp(6), 0, 0);
        card.addView(detail);
    }

    private void addButton(String value, boolean primary, Runnable click) {
        TextView b = text(value, 14, primary ? WHITE : PURPLE, true);
        b.setGravity(Gravity.CENTER);
        b.setPadding(dp(14), dp(13), dp(14), dp(13));
        b.setBackground(shape(primary ? PURPLE : 0xFFECE9FF, 15));
        b.setFocusable(true);
        b.setClickable(true);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(9);
        content.addView(b, p);
        b.setOnClickListener(v -> click.run());
    }

    private String value(String english, String hindiValue) {
        return hindi ? hindiValue : english;
    }

    private int completedCount() {
        int count = 0;
        for (Skill s : skills) if (prefs.getBoolean("done_" + s.id, false)) count++;
        return count;
    }

    private void showHome() {
        setup(value("Real-World Skills", "जीवन के असली कौशल"));
        addCard(value("Skills for everyday life", "हर दिन के काम आने वाले कौशल"),
            value("Short scenarios + real-world practice. No grades, no pressure. Progress stays on this device.",
                 "छोटे उदाहरण और असली जीवन का अभ्यास। कोई अंक या दबाव नहीं। प्रगति इसी डिवाइस पर रहती है।"),
            0xFFECE9FF);
        int done = completedCount();
        addCard(value("Your progress", "तुम्हारी प्रगति"),
            (hindi ? "अभ्यास किए गए कौशल: " : "Skills practised: ") + done + " / " + skills.length,
            0xFFE2F2E9);
        for (int i = 0; i < skills.length; i++) {
            final int index = i;
            Skill s = skills[i];
            boolean doneSkill = prefs.getBoolean("done_" + s.id, false);
            String title = s.icon + "  " + value(s.title, s.titleHi) + (doneSkill ? "  ✓" : "");
            String detail = value(s.area, s.areaHi) + " · " + value(s.goal, s.goalHi);
            addCard(title, detail + "\n" + value("Open practice  →", "अभ्यास खोलें  →"), doneSkill ? 0xFFE8F4DE : WHITE);
            content.getChildAt(content.getChildCount() - 1).setOnClickListener(v -> openSkill(index));
        }
        addCard(value("A note for grown-ups", "बड़ों के लिए"),
            value("Some missions need an adult nearby. Children should never handle dangerous tools, medicines, money accounts or electrical equipment alone.",
                 "कुछ गतिविधियों में बड़े का साथ ज़रूरी है। बच्चों को ख़तरनाक औज़ार, दवा, बैंक खाते या बिजली के उपकरण अकेले नहीं सँभालने चाहिए।"),
            0xFFFFEBCB);
    }

    private void openSkill(int index) {
        answeredCorrect = false;
        practiceChecks = new boolean[skills[index].steps.length];
        renderSkill(index);
    }

    private void renderSkill(int index) {
        Skill s = skills[index];
        setup(value(s.icon + "  " + s.title, s.icon + "  " + s.titleHi));
        addButton(value("← All skills", "← सभी कौशल"), false, this::showHome);
        addCard(value("What you will build", "तुम क्या सीखोगे"),
            value(s.goal, s.goalHi), 0xFFECE9FF);
        addCard(value("Think about this", "इस बारे में सोचो"),
            value(s.scenario, s.scenarioHi), 0xFFFFEBCB);

        String[] opts = hindi ? s.optionsHi : s.options;
        for (int i = 0; i < opts.length; i++) {
            final int choice = i;
            addButton((i + 1) + ".  " + opts[i], false, () -> {
                if (choice == s.answer) {
                    answeredCorrect = true;
                    renderSkill(index);
                } else {
                    Toast.makeText(this, value("Good try. Think about safety and kindness, then try again.",
                        "अच्छी कोशिश। सुरक्षा और दयालुता के बारे में सोचकर फिर कोशिश करो।"), Toast.LENGTH_LONG).show();
                }
            });
        }

        if (answeredCorrect) {
            addCard(value("Why this is a good choice", "यह अच्छा चुनाव क्यों है"),
                value(s.feedback, s.feedbackHi), 0xFFE8F4DE);
            addCard(value("Real-life mission", "असली जीवन का अभ्यास"),
                value(s.mission, s.missionHi), 0xFFDFF3FF);
            addCard(value("Try these steps", "ये चरण आज़माओ"),
                value("Tick each step after you have tried it. Ask an adult to help where needed.",
                     "हर चरण आज़माने के बाद उस पर निशान लगाओ। जहाँ ज़रूरत हो, बड़े की मदद लो।"),
                WHITE);
            for (int i = 0; i < s.steps.length; i++) {
                final int step = i;
                CheckBox check = new CheckBox(this);
                check.setText(hindi ? s.stepsHi[i] : s.steps[i]);
                check.setTextSize(14);
                check.setTextColor(INK);
                check.setPadding(dp(5), dp(7), dp(5), dp(7));
                check.setChecked(practiceChecks[i]);
                check.setOnCheckedChangeListener((button, checked) -> practiceChecks[step] = checked);
                content.addView(check);
            }
            addCard(value("Safety first", "सुरक्षा सबसे पहले"),
                value(s.safety, s.safetyHi), 0xFFFFEBCB);
            addCard(value("Think back", "पीछे मुड़कर सोचो"),
                value(s.reflection, s.reflectionHi), WHITE);
            boolean alreadyDone = prefs.getBoolean("done_" + s.id, false);
            addButton(alreadyDone ? value("Practised ✓", "अभ्यास किया ✓") :
                value("I tried this mission ✓", "मैंने यह अभ्यास किया ✓"), true, () -> {
                    boolean all = true;
                    for (boolean checked : practiceChecks) if (!checked) all = false;
                    if (!all) {
                        Toast.makeText(this, value("Try each practice step before marking it complete.",
                            "पूरा करने से पहले हर अभ्यास चरण आज़माओ।"), Toast.LENGTH_LONG).show();
                        return;
                    }
                    prefs.edit().putBoolean("done_" + s.id, true).apply();
                    Toast.makeText(this, value("Skill practice saved on this device.",
                        "कौशल का अभ्यास इसी डिवाइस पर सहेजा गया।"), Toast.LENGTH_LONG).show();
                    showHome();
                });
        } else {
            addCard(value("How to use this activity", "इस गतिविधि का उपयोग कैसे करें"),
                value("Choose the most helpful response. Then try the real-life mission and tick the steps you practise.",
                     "सबसे मददगार जवाब चुनो। फिर असली जीवन का अभ्यास करो और आज़माए गए चरणों पर निशान लगाओ।"),
                WHITE);
        }
        addButton(value("Back to skills", "कौशल सूची पर लौटें"), false, this::showHome);
    }
}
