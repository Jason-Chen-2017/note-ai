package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.light.noteai.LLMUtil.buildBlogPrompt
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_DeepSeek_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://127.0.0.1:5000/v1/chat/completions",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}


/**
 * POST:
 * http://127.0.0.1:5000/v1/chat/completions
 *
 * {
 *      "model": "deepseek-ai_deepseek-moe-16b-chat",
 *      "messages": [{"role": "user", "content": "注意力机制原理说明，注意力分计算数学公式（latex格式）"}],
 *      "temperature": 0.1
 * }
 *
 *
 * {
 *   "id": "chatcmpl-1705510091602157568",
 *   "object": "chat.completions",
 *   "created": 1705510091,
 *   "model": "deepseek-ai_deepseek-moe-16b-chat",
 *   "choices": [
 *     {
 *       "index": 0,
 *       "finish_reason": "stop",
 *       "message": {
 *         "role": "assistant",
 *         "content": "注意力机制是一种用于处理序列数据的机制，它允许模型在处理序列时能够根据序列的上下文信息来调整其注意力权重。注意力机制的核心思想是，模型在处理序列时，不是简单地对每个元素进行相同的处理，而是根据序列的上下文信息来调整每个元素的处理方式。\n\n注意力机制的数学公式可以表示为：\n\n$Attention(Q, K, V) = softmax(\\frac{QK^T}{\\sqrt{d}})V$\n\n其中，$Q$ 是查询向量，$K$ 是键向量，$V$ 是值向量。$d$ 是向量的维度。\n\n在注意力机制中，模型首先将查询向量和键向量进行点积运算，得到一个权重矩阵。然后，模型将权重矩阵与值向量进行点积运算，得到一个加权和的结果。最后，模型将加权和的结果进行归一化，得到最终的输出。\n\n注意力机制在自然语言处理、计算机视觉等领域有着广泛的应用。例如，在机器翻译中，注意力机制可以帮助模型更好地理解源语言的句子结构，从而生成更加准确的翻译结果。在图像识别中，注意力机制可以帮助模型更好地关注图像中的关键部分，从而提高模型的识别准确率。"
 *       }
 *     }
 *   ],
 *   "usage": {
 *     "prompt_tokens": 45,
 *     "completion_tokens": 279,
 *     "total_tokens": 324
 *   }
 * }
 */
object DeepSeekUtil {

    fun GetAPI(): String {
        val API = Get_DeepSeek_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }

    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", buildBlogPrompt(prompt))))
        data["messages"] = messages

        data["temperature"] = 0.7
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        return ParseDeepSeekResponse(res)
    }


    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", prompt)))
        data["messages"] = messages

        data["temperature"] = 0.7
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        return ParseDeepSeekResponse(res)
    }


    private fun ParseDeepSeekResponse(res: String): String {

        val gson = Gson()
        val LMResponse = gson.fromJson(res, DeepSeekResponse::class.java)

        if (LMResponse.choices.isNotEmpty()) {
            return LMResponse.choices[0].message.content
        } else {
            return "\n"
        }

    }

    class DeepSeekResponse {
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



}
