package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.light.noteai.LLMUtil.buildBlogPrompt
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_OpenChat_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://localhost:18888/v1/chat/completions",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}


/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object OpenChatUtil {

    fun GetAPI(): String {
        val API = Get_OpenChat_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }


    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}

    Sampling parameters for text generation.

    Overall, we follow the sampling parameters from the OpenAI text completion
    API (https://platform.openai.com/docs/api-reference/completions/create).
    In addition, we support beam search, which is not supported by OpenAI.

    Args:
        n: Number of output sequences to return for the given prompt.
        best_of: Number of output sequences that are generated from the prompt.
            From these `best_of` sequences, the top `n` sequences are returned.
            `best_of` must be greater than or equal to `n`. This is treated as
            the beam width when `use_beam_search` is True. By default, `best_of`
            is set to `n`.
        presence_penalty: Float that penalizes new tokens based on whether they
            appear in the generated text so far. Values > 0 encourage the model
            to use new tokens, while values < 0 encourage the model to repeat
            tokens.
        frequency_penalty: Float that penalizes new tokens based on their
            frequency in the generated text so far. Values > 0 encourage the
            model to use new tokens, while values < 0 encourage the model to
            repeat tokens.
        repetition_penalty: Float that penalizes new tokens based on whether
            they appear in the generated text so far. Values > 1 encourage the
            model to use new tokens, while values < 1 encourage the model to
            repeat tokens.
        temperature: Float that controls the randomness of the sampling. Lower
            values make the model more deterministic, while higher values make
            the model more random. Zero means greedy sampling.
        top_p: Float that controls the cumulative probability of the top tokens
            to consider. Must be in (0, 1]. Set to 1 to consider all tokens.
        top_k: Integer that controls the number of top tokens to consider. Set
            to -1 to consider all tokens.
        min_p: Float that represents the minimum probability for a token to be
            considered, relative to the probability of the most likely token.
            Must be in [0, 1]. Set to 0 to disable this.
        use_beam_search: Whether to use beam search instead of sampling.
        length_penalty: Float that penalizes sequences based on their length.
            Used in beam search.
        early_stopping: Controls the stopping condition for beam search. It
            accepts the following values: `True`, where the generation stops as
            soon as there are `best_of` complete candidates; `False`, where an
            heuristic is applied and the generation stops when is it very
            unlikely to find better candidates; `"never"`, where the beam search
            procedure only stops when there cannot be better candidates
            (canonical beam search algorithm).
        stop: List of strings that stop the generation when they are generated.
            The returned output will not contain the stop strings.
        stop_token_ids: List of tokens that stop the generation when they are
            generated. The returned output will contain the stop tokens unless
            the stop tokens are sepcial tokens.
        ignore_eos: Whether to ignore the EOS token and continue generating
            tokens after the EOS token is generated.
        max_tokens: Maximum number of tokens to generate per output sequence.
        logprobs: Number of log probabilities to return per output token.
            Note that the implementation follows the OpenAI API: The return
            result includes the log probabilities on the `logprobs` most likely
            tokens, as well the chosen tokens. The API will always return the
            log probability of the sampled token, so there  may be up to
            `logprobs+1` elements in the response.
        prompt_logprobs: Number of log probabilities to return per prompt token.
        skip_special_tokens: Whether to skip special tokens in the output.
        spaces_between_special_tokens: Whether to add spaces between special
            tokens in the output.  Defaults to True.
        logits_processors: List of functions that modify logits based on
            previously generated tokens.

     */
    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["model"] = "openchat_3.5"
        data["condition"] = "Code"

        data["top_p"] = 0.9
        data["temperature"] = 0.1

        data["presence_penalty"] = 0.5
        data["frequency_penalty"] = 0.5
        data["repetition_penalty"] = 1.5

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", buildBlogPrompt(prompt))))
        data["messages"] = messages

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        println(res)

        return ParseOpenChatResponse(res)
    }


    /**
     * Input:
     * {
     *     "model": "openchat_3.5",
     *     "condition": "Code",
     *     "messages": [{"role": "user", "content": "1+1="}]
     *   }
     *
     * Output:
     *   {
     *   "id": "cmpl-bafc983ab99848c1b32a83cd35181377",
     *   "object": "chat.completion",
     *   "created": 1701184195,
     *   "model": "openchat_3.5",
     *   "choices": [
     *     {
     *       "index": 0,
     *       "message": {
     *         "role": "assistant",
     *         "content": "2"
     *       },
     *       "finish_reason": "stop"
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 13,
     *     "total_tokens": 16,
     *     "completion_tokens": 3
     *   }
     * }
     */
    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["model"] = "openchat_3.5"
        data["condition"] = "Code"

        data["top_p"] = 0.9
        data["temperature"] = 0.1

        data["presence_penalty"] = 0.5
        data["frequency_penalty"] = 0.5
        data["repetition_penalty"] = 1.5

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", prompt)))
        data["messages"] = messages

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        return ParseOpenChatResponse(res)
    }


    private fun ParseOpenChatResponse(res: String): String {

        val gson = Gson()
        val OpenChatResponse = gson.fromJson(res, OpenChatResponse::class.java)

        if (OpenChatResponse.choices.isNotEmpty()) {
            val text = OpenChatResponse.choices[0].message.content
            println("text=$text")

            val completeText =
                """                 

# 1.背景介绍

${text}

"""

            return completeText

        } else {
            return "\n"
        }

    }


    /**
     * {
     *   "id": "cmpl-bafc983ab99848c1b32a83cd35181377",
     *   "object": "chat.completion",
     *   "created": 1701184195,
     *   "model": "openchat_3.5",
     *   "choices": [
     *     {
     *       "index": 0,
     *       "message": {
     *         "role": "assistant",
     *         "content": "2"
     *       },
     *       "finish_reason": "stop"
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 13,
     *     "total_tokens": 16,
     *     "completion_tokens": 3
     *   }
     * }
     */
    class OpenChatResponse {
        var id = "cmpl-bafc983ab99848c1b32a83cd35181377"
        var model = "openchat_3.5"
        var choices: List<Choices> = listOf()
    }

    class Choices {
        var index = 0
        var message = Message()
    }

    class Message {
        var role = "assistant"
        var content = ""
    }


    fun replaceNewLineAndTab(text: String): String {
        // 代码中的换行 制表符的处理
        var txt = text
        txt = txt.replace(
            "\n",
            """
"""
        )

        txt = txt.replace("\t", """    """)
        // fix:  heta --->\theta
        txt = fixLatex(txt)

        // 去掉所有的行首空格数=第一行空格数量
        txt = trimHeadSpaces(txt)

        return txt
    }

    fun fixLatex(txt: String): String {
        var text = txt

        text = text.replace("    heta", "\\theta")
        text = text.replace("    ext", "\\text")
        text = text.replace("\nabla", "\\nabla")

        return text
    }


    /*
    去掉所有的行首空格数=首次遇到开头有空格行的数量
    例如：txt=
    123
    456
      789
      666
    处理之后：
    123
    456
    789
    666
     */
    fun trimHeadSpaces(txt: String): String {
        val lines = txt.lines() // 将文本按行分割成列表

        val firstLineSpaces = lines.indexOfFirst { it.trim().isNotEmpty() && it[0] == ' ' } // 获取首次遇到开头有空格行的数量

        val trimmedLines = lines.mapIndexed { index, line ->
            if (index < firstLineSpaces) {
                line // 在首次遇到开头有空格行之前的行不做处理
            } else {
                if (firstLineSpaces > 0) {
                    val trimmedSpaces = line.takeWhile { it == ' ' }.take(firstLineSpaces) // 获取指定数量的空格
                    line.removePrefix(trimmedSpaces) // 去除指定数量的空格
                } else {
                    line
                }
            }
        }

        return trimmedLines.joinToString("\n") // 将处理后的行重新组合成文本
    }

}
