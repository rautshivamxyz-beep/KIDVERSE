#!/usr/bin/env python3
"""Validate KIDVERSE lesson JSON using only the Python standard library."""
import json
import sys
from pathlib import Path

FILES = (
    Path("app/src/main/assets/library.json"),
    Path("content/packs/knowledge-pack-01.json"),
)
errors = []
global_ids = set()


def require_text(obj, key, where):
    value = obj.get(key)
    if not isinstance(value, str) or not value.strip():
        errors.append(f"{where}: missing or empty '{key}'")


def validate_file(path):
    try:
        with path.open("r", encoding="utf-8") as handle:
            payload = json.load(handle)
    except Exception as exc:
        errors.append(f"{path}: cannot read valid UTF-8 JSON ({exc})")
        return 0

    topics = payload.get("topics")
    if not isinstance(topics, list) or not topics:
        errors.append(f"{path}: 'topics' must be a non-empty array")
        return 0

    local_ids = set()
    for index, topic in enumerate(topics):
        where = f"{path}: topic[{index}]"
        if not isinstance(topic, dict):
            errors.append(f"{where}: must be an object")
            continue

        topic_id = topic.get("id")
        if not isinstance(topic_id, str) or not topic_id.strip():
            errors.append(f"{where}: missing topic id")
        else:
            if topic_id in local_ids:
                errors.append(f"{where}: duplicate id '{topic_id}' in this file")
            if topic_id in global_ids:
                errors.append(f"{where}: id '{topic_id}' is already used by the other content file")
            local_ids.add(topic_id)
            global_ids.add(topic_id)

        for key in ("subject", "title", "titleHi", "classLabel", "curriculum", "sequenceNote"):
            require_text(topic, key, where)

        grade = topic.get("class")
        if isinstance(grade, bool) or not isinstance(grade, int) or not 1 <= grade <= 5:
            errors.append(f"{where}: class must be an integer from 1 to 5")
        elif topic.get("classLabel") != f"Class {grade}":
            errors.append(f"{where}: classLabel must match Class {grade}")

        chapter = topic.get("chapterNo")
        if isinstance(chapter, bool) or not isinstance(chapter, int) or chapter < 1:
            errors.append(f"{where}: chapterNo must be a positive integer")

        has_simple = isinstance(topic.get("simple"), str) and topic["simple"].strip()
        has_intro = isinstance(topic.get("intro"), str) and topic["intro"].strip()
        sections = topic.get("sections", [])
        if not has_simple and not has_intro and not (isinstance(sections, list) and sections):
            errors.append(f"{where}: no lesson explanation was found")

        if "simple" in topic:
            require_text(topic, "simple", where)
            require_text(topic, "simpleHi", where)
        if "deep" in topic:
            require_text(topic, "deep", where)
            require_text(topic, "deepHi", where)
        if "intro" in topic:
            require_text(topic, "intro", where)
            require_text(topic, "introHi", where)

        if sections is not None:
            if not isinstance(sections, list):
                errors.append(f"{where}: sections must be an array")
            else:
                for section_index, section in enumerate(sections):
                    sw = f"{where}: section[{section_index}]"
                    if not isinstance(section, dict):
                        errors.append(f"{sw}: must be an object")
                        continue
                    for key in ("heading", "headingHi", "body", "bodyHi"):
                        require_text(section, key, sw)

        practice = topic.get("practice", [])
        if practice is not None:
            if not isinstance(practice, list):
                errors.append(f"{where}: practice must be an array")
            else:
                for question_index, question in enumerate(practice):
                    qw = f"{where}: practice[{question_index}]"
                    if not isinstance(question, dict):
                        errors.append(f"{qw}: must be an object")
                        continue
                    for key in ("question", "questionHi", "explanation", "explanationHi"):
                        require_text(question, key, qw)
                    options = question.get("options")
                    options_hi = question.get("optionsHi")
                    if not isinstance(options, list) or not 2 <= len(options) <= 6:
                        errors.append(f"{qw}: options must contain 2 to 6 choices")
                        continue
                    if not isinstance(options_hi, list) or len(options_hi) != len(options):
                        errors.append(f"{qw}: Hindi options must match English options")
                    elif any(not isinstance(option, str) or not option.strip() for option in options + options_hi):
                        errors.append(f"{qw}: choices must be non-empty strings")
                    correct = question.get("correct")
                    if isinstance(correct, bool) or not isinstance(correct, int) or not 0 <= correct < len(options):
                        errors.append(f"{qw}: correct answer index is outside the options array")

    print(f"{path}: checked {len(topics)} topics")
    return len(topics)


def main():
    total = sum(validate_file(path) for path in FILES)
    if errors:
        print("\nContent validation failed:", file=sys.stderr)
        for error in errors:
            print(f" - {error}", file=sys.stderr)
        print(f"\n{len(errors)} error(s) across {total} topics.", file=sys.stderr)
        return 1
    print(f"Content validation passed: {total} topics checked; ids and quiz answer indexes are valid.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
