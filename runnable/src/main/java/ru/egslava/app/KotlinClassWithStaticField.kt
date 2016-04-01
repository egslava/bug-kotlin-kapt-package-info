package ru.egslava.app

open class KotlinClassWithStaticField {
    companion object {
        @JvmField val FIELD = object : Any(){}
        // originally for using with FasterXML/Jackson
        // @JvmField val TYPE = object : TypeReference<KotlinClassWithStaticField>() {}
    }
}
