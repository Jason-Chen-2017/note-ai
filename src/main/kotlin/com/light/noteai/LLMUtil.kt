package com.light.noteai


import java.util.*


enum class LLM_MODEL {Open_Chat_MODEL, QWen_MODEL, ChatGLM_MODEL, WizardLM_MODEL }

// 配置模型
val USE_LLM_MODEL = LLM_MODEL.Open_Chat_MODEL

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

