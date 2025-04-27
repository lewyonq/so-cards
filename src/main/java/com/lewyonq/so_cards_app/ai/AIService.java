package com.lewyonq.so_cards_app.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    ChatLanguageModel gemini = GoogleAiGeminiChatModel.builder()
            .apiKey(System.getenv("GEMINI_KEY"))
            .modelName("gemini-2.0-flash")
            .responseFormat(ResponseFormat.JSON)
            .logRequestsAndResponses(true)
            .build();

    public String chat(String message) {
        return gemini.chat(message);
    }

    public String getMultipleChoiceJsonPrompt(String quizData) {
        String promptTemplate = """
                **Role:** You are an AI assistant specialized in creating educational content, specifically multiple-choice quizzes designed to test understanding, not just memorization.

                **Objective:** Your task is to transform a given list of question-answer pairs (provided in JSON format) into a more detailed JSON format suitable for an ABCD multiple-choice test. For each question, you must **rephrase the correct answer** and generate three plausible but incorrect distractors.

                **Input Data Format:**
                You will receive input data as a JSON array of objects. Each object represents a single question-answer pair and has the following structure:

                ```
                [
                  {
                    "question": "<question_text>",
                    "answer": <correct_answer_value> // This is the concept/fact to represent
                  },
                  // ... more question-answer pairs
                ]
                ```
                *   `<unique_identifier>` can be a number or string.
                *   `<question_text>` is the text of the question.
                *   `<correct_answer_value>` is the original correct answer (can be a string, number, etc.).

                **Output Data Format Requirements:**
                You must generate a JSON array of objects as output. Each object in the output array corresponds to one object in the input array and must have the following structure:

                ```
                [
                  {
                    "question": "<same_question_text_as_input>",
                    "a": { "value": <option_value_A>, "correct": <boolean> },
                    "b": { "value": <option_value_B>, "correct": <boolean> },
                    "c": { "value": <option_value_C>, "correct": <boolean> },
                    "d": { "value": <option_value_D>, "correct": <boolean> }
                  },
                  // ... more generated test questions
                ]
                ```

                **Detailed Instructions for Generating Each Test Question:**

                1.  **Identify Correct Answer Concept:** For each input object, understand the meaning and factual concept represented by the `answer` value.
                2.  **Rephrase the Correct Answer:** **Crucially, rewrite or rephrase the original `answer` value.** Use different wording or structure, but ensure the core meaning remains *exactly identical* and factually correct. This rephrased value will be used for the `value` field of the option marked `correct: true`.
                    *   *Goal:* Test understanding, not just recognition of the original answer text/value.
                    *   *Example 1:* If input `answer` is `"Paris"`, the rephrased `value` for the correct option might be `"France's capital city"`.
                    *   *Example 2:* If input `answer` is `1914` keep `1914` if rephrasing is awkward or less clear, but *try* to rephrase meaningfully where possible.
                    *   *Constraint:* The rephrased answer must unambiguously be the correct answer to the original `question`.
                3.  **Generate Distractors:** Create exactly three (3) incorrect answer options (distractors) based on the question's topic. These distractors **must** be:
                    *   **Plausible:** They should seem like potentially correct answers.
                    *   **Related:** They should relate to the question's topic.
                    *   **Distinct:** They must be clearly different in meaning from the *rephrased* correct answer.
                    *   **Similar Format (Guideline):** Try to keep distractors in a similar format/type to the *rephrased* correct answer (e.g., if the rephrased answer is a descriptive string, distractors should also be descriptive strings or plausible concepts).
                    *   **Tricky/Misleading (Optional but Preferred):** Based on common misconceptions if possible.
                    *   **Unambiguously Incorrect:** Definitely wrong answers to the question.
                4.  **Ensure Single Correct Answer:** Exactly one of the four options (`a`, `b`, `c`, `d`) must contain the *rephrased correct answer* (from Step 2) in its `value` field and have its `correct` field set to `true`. All other three options (the distractors from Step 3) must have `correct: false`. Make sure, that the correct answer is randomly `a`, `b`, `c` or `d`.
                5.  **Randomize Position:** The rephrased correct answer should be randomly `a`, `b`, `c` or `d`. Randomly assign the rephrased correct answer and the three distractors to the positions `a`, `b`, `c`, and `d` for each question. Make sure to assign correct answers equally, it means that for 12 questions, every answer `a`, `b`, `c`, and `d` will be correct 3 times. If there will be 10 questions, it might be `a` and `b` each 3 times correct, `c` and `d` each 2 times correct. and so on.
                6.  **Maintain Data Types (Guideline):** Ensure the `value` field for each option uses an appropriate data type. While rephrasing might sometimes necessitate a type change (e.g., number to descriptive string), try to maintain consistency among the options (`a` through `d`) for a given question where it makes sense. The rephrased correct answer sets the expectation for the distractors' format.
                7.  **Preserve ID and Question:** The `id` and `question` fields in the output object must be identical to those in the corresponding input object.
                8.  **Valid JSON:** The entire output must be a single, valid JSON array. Ensure correct syntax.

                **Example Transformation (Illustrating Rephrasing):**

                *   **Input:**
                    ```
                    [
                      { "question": "What is the capital of France?", "answer": "Paris" }
                    ]
                    ```
                *   **Potential Output (Correct answer 'b', rephrased):**
                    ```
                    [
                      {
                        "question": "What is the capital of France?",
                        "a": { "value": "Lyon", "correct": false }, // Distractor city
                        "b": { "value": "France's capital city", "correct": true }, // Rephrased correct answer
                        "c": { "value": "Marseille", "correct": false }, // Distractor city
                        "d": { "value": "The largest city in Germany", "correct": false } // Related but incorrect concept
                      }
                    ]
                    ```

                **Task Execution:**
                Now, please process the following input JSON data according to *all* the instructions above (including rephrasing the correct answer) and generate the corresponding ABCD test format JSON output.

                ```
                %s
                ```
                """;

        return promptTemplate.formatted(quizData);
    }
}
