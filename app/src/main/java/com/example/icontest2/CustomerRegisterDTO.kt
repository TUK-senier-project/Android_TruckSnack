package com.example.icontest2

import com.google.gson.annotations.SerializedName


data class CustomerRegisterDTO(
    // 우리가 서버에 보내야할 요구사항들 즉 데이터 클래스 / 형식을 맞춰줘야한다
    // 백엔드 깃허브에서 도메인 > 알맞은 위치 > (nullable이 false인것은 값이 있어야만 함) > 확인 후 뒤로 가서 관련 컨트롤러 > 성공 201 실패 400
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("location") val location: String



)
