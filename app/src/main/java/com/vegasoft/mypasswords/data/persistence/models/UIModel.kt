package com.vegasoft.mypasswords.data.persistence.models

interface UIModel<T> {
    fun toUIModel(): T
}