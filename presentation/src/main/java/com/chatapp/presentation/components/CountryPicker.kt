package com.chatapp.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.chatapp.presentation.model.CountryDataUIModel
import com.chatapp.ui.MaskVisualTransformation
import com.chatapp.ui.R
import com.chatapp.ui.theme.LightPink
import com.chatapp.ui.theme.Pink
import kotlinx.coroutines.Dispatchers

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun CountryPicker(
    countries: List<CountryDataUIModel>,
    selectedCountry: CountryDataUIModel?,
    phoneNumber: String,
    isError: Boolean,
    onCountrySelected: (CountryDataUIModel) -> Unit,
    onPhoneNumberChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CommonOutlinedTextField(
            modifier = Modifier.onGloballyPositioned { layoutCoordinates -> rowSize = layoutCoordinates.size.toSize() },
            textValue = phoneNumber,
            onTextValueChanged = onPhoneNumberChanged,
            visualTransformation = MaskVisualTransformation(selectedCountry?.mask.orEmpty()),
            label = "Введите номер телефона",
            isError = isError,
            supportingText = "Номер введен не до конца"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                SvgImage(
                    url = selectedCountry?.flag.orEmpty(),
                    contentDescription = "${selectedCountry?.name} flag",
                    modifier = Modifier
                        .width(50.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { expanded = true }
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() })
                .background(color = Pink)
                .border(width = 2.dp, color = LightPink, shape = RoundedCornerShape(16.dp))
                .heightIn(max = 300.dp)
        ) {
            if (countries.isEmpty()) {
                Text("No countries available", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.size(400.dp)) {
                    items(countries.size) { index ->
                        DropdownMenuItem(
                            onClick = {
                                onCountrySelected(countries[index])
                                expanded = false
                            },
                            text = { Text(text = "${countries[index].name} +${countries[index].code}") },
                            leadingIcon = {
                                SvgImage(
                                    url = countries[index].flag,
                                    modifier = Modifier
                                        .width(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SvgImage(
    url: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val placeholder = R.drawable.flag_placeholder

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .decoderFactory(SvgDecoder.Factory())
            .size(Size.ORIGINAL)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}

