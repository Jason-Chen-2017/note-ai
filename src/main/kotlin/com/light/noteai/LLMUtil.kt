package com.light.noteai


import java.util.*


enum class LLM_MODEL { Open_Chat_MODEL, QWen_MODEL, ChatGLM_MODEL, WizardLM_MODEL }

// 配置模型
val USE_LLM_MODEL = LLM_MODEL.Open_Chat_MODEL
//val USE_LLM_MODEL = LLM_MODEL.ChatGLM_MODEL
//val USE_LLM_MODEL = LLM_MODEL.WizardLM_MODEL

object LLMUtil {

    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}
     */
    fun WriteBlog(prompt: String): String {
        if (USE_LLM_MODEL == LLM_MODEL.QWen_MODEL) {
            return QianWenUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.WizardLM_MODEL) {
            return WizardLMUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.ChatGLM_MODEL) {
            return ChatGLMUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Open_Chat_MODEL) {
            return OpenChatUtil.WriteBlog(prompt)
        } else {
            return WizardLMUtil.WriteBlog(prompt)
        }
    }


    fun buildBlogPrompt(prompt: String): String {
        val fullPrompt = """
现在你是一位资深大数据技术专家,人工智能科学家,计算机科学家，资深程序员和软件系统资深架构师，CTO。

GOALS:

请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客文章。
文章核心内容必须包含：
# 1.背景介绍
# 2.核心概念与联系
# 3.核心算法原理和具体操作步骤以及数学模型公式详细讲解
# 4.具体代码实例和详细解释说明
# 5.未来发展趋势与挑战
# 6.附录常见问题与解答
这6大部分。

CONSTRAINTS:
1.文章字数大于8000字
2.使用markdown格式
3.数学模型公式请使用latex格式，嵌入文中使用 $$ 
4.文章末尾不要列出参考文献

下面我们开始写文章的全部内容。

# ${prompt}
# 1.背景介绍
""".trimIndent()

        println(Date())
        println(fullPrompt)

        return fullPrompt
    }


    fun Complete(prompt: String): String {
        if (USE_LLM_MODEL == LLM_MODEL.QWen_MODEL) {
            return QianWenUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.WizardLM_MODEL) {
            return WizardLMUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.ChatGLM_MODEL) {
            return ChatGLMUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Open_Chat_MODEL) {
            return OpenChatUtil.Complete(prompt)
        } else {
            return WizardLMUtil.Complete(prompt)
        }
    }

}


// 随机返回 apiList 中的一个元素
fun Get_LLM_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://127.0.0.1:5000/api/v1/generate",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}

