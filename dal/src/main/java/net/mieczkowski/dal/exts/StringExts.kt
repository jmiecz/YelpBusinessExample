package net.mieczkowski.dal.exts

import com.fasterxml.jackson.core.type.TypeReference
import net.mieczkowski.dal.RetrofitFactory

/**
 * Created by Josh Mieczkowski on 9/11/2018.
 */

internal fun <T> String.convertJsonToList(): List<T> {
    return RetrofitFactory.objectMapper.readValue(this, object : TypeReference<List<T>>() {})
}