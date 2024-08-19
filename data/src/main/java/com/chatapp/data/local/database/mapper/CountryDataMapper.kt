package com.chatapp.data.local.database.mapper

import com.chatapp.data.local.database.entity.CountryData
import com.chatapp.domain.model.CountryModel

fun List<CountryModel>.toData(): List<CountryData> =
    this.map { model ->
        CountryData(
            name = model.name,
            code = model.code,
            flag = model.flag,
            iso = model.iso,
            mask = model.mask
        )
    }

fun List<CountryData>.toDomain(): List<CountryModel> = this.map { model ->
        CountryModel(
            name = model.name,
            code = model.code,
            flag = model.flag,
            iso = model.iso,
            mask = model.mask
        )
    }