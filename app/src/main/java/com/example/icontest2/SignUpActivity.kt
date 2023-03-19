package com.example.icontest2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.icontest2.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private var TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val returnIntent = Intent(this, LoginActivity::class.java)
        val locationRegisterIntent = Intent(this, LocationRegisterActivity::class.java)

        // 아이디, 비밀번호, 비밀번호 확인, 이름, 휴대폰번호의 editText id
        val signUpIdEdit = binding.signUpIdEdit
        val signUpPasswdEdit = binding.signUpPasswdEdit
        val signUpCheckPasswdEdit = binding.signUpCheckPasswdEdit
        val signUpNameEdit = binding.signUpNameEdit
        val signUpPhoneEdit = binding.signUpPhoneEdit

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            // 사용자의 입력이 끝난 후 처리
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "${s}")

                // 아이디
                if (s == signUpIdEdit.editableText){
                    Log.d(TAG, "in - id - length")
                    val minLength = 5
                    val maxLength = 15
                    checkWhiteSpace(s, signUpIdEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpIdEdit) // 특수 문자 확인
                    checkLength(s, signUpIdEdit, maxLength, minLength) // 문자열 길이 확인

                }
                // 비밀번호
                if (s == signUpPasswdEdit.editableText){
                    Log.d(TAG, "in - pw - length")
                    val minLength = 8
                    val maxLength = 20
                    checkWhiteSpace(s, signUpPasswdEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpPasswdEdit) // 특수 문자 확인
                    checkAlphaNumber(signUpPasswdEdit) // 영문 + 숫자 확인
                    checkLength(s, signUpPasswdEdit, maxLength, minLength) // 문자열 길이 확인

                }
                // 비밀번호 체크
                if (s == signUpCheckPasswdEdit.editableText){
                    Log.d(TAG, "in - pwcheck - length")
                    if (signUpPasswdEdit.text.toString() != signUpCheckPasswdEdit.text.toString()){
                        signUpCheckPasswdEdit.error = "비밀번호가 일치하지 않습니다."
                    } else {
                        signUpCheckPasswdEdit.error = null
                    }

                }
                // 이름
                if (s == signUpNameEdit.editableText){
                    Log.d(TAG, "in - name - length")
                    val minLength = 1
                    val maxLength = 10
                    checkWhiteSpace(s, signUpNameEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpNameEdit) // 특수 문자 확인
                    checkKorean(signUpNameEdit) // 한글 확인
                    checkLength(s, signUpNameEdit, maxLength, minLength) // 문자열 길이 확인
                }
                // 휴대폰번호
                if (s == signUpPhoneEdit.editableText){
                    Log.d(TAG, "in - phone - length")
                    checkWhiteSpace(s, signUpPhoneEdit) // 공백 문자 확인
                    checkSpecialCharacters(s, signUpPhoneEdit) // 특수 문자 확인
                    checkPhoneNumber(signUpPhoneEdit) // 숫자 및 11자 확인
                }
            }
        }

        // 각 항목별 공백, 특수 문자 처리
        signUpIdEdit.addTextChangedListener(textWatcher)
        signUpPasswdEdit.addTextChangedListener(textWatcher)
        signUpCheckPasswdEdit.addTextChangedListener(textWatcher)
        signUpNameEdit.addTextChangedListener(textWatcher)
        signUpPhoneEdit.addTextChangedListener(textWatcher)





        binding.signUpBtn.setOnClickListener {

            returnIntent.putExtra("name", binding.signUpNameEdit.text.toString())
            startActivity(returnIntent)
            finish()
        }

        binding.signUpLocationBtn.setOnClickListener {
            startActivity(locationRegisterIntent)
        }


    }
    // 공백 문자 확인 함수
    fun checkWhiteSpace(editable: Editable?, editText: EditText){
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkWhiteSpace")
        // 결과 출력
        if(text.contains(" ")){
            Log.d(TAG, "${text} - 공백있음")
            editText.error = "공백이 포함되어 있습니다."
        }
    }

    // 특수 문자 확인 함수
    fun checkSpecialCharacters(editable: Editable?, editText: EditText){
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkSpecialCharacters")

        // 검사할 특수 문자 지정
        val specialCharacters = "!@#$%^&*()_-+=|\\{}[]:;\"'<>,.?/~`"

        // 결과 출력
        if (text.matches(".*[!@#\$%^&*(),.?\":{}|<>\\[\\]~-].*".toRegex())) {
            editText.error = "특수문자는 입력할 수 없습니다."
        }

    }

    // 문자 길이 확인 함수
    fun checkLength(editable: Editable?, editText: EditText ,maxLength: Int, minLength: Int){
        // EditText의 문자열 가져오기
        val text = editable.toString()
        Log.d(TAG, "${text} - checkLength")

        val length = text.length

        if (length < minLength) {
            editText.error = "최소 ${minLength}자 이상 입력하세요"
        } else if (length > maxLength) {
            editText.error = "최대 ${maxLength}자까지 입력 가능합니다"
        }

    }

    // 영문 + 숫자 확인 함수
    fun checkAlphaNumber(editText: EditText){
        Log.d(TAG, " - checkAlphaNumber")

        val alphaNumbericRegex = Regex("[a-zA-Z0-9]+")
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val input = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length).toString()
            return@InputFilter if (input.matches(alphaNumbericRegex)) null else ""
        }
        editText.filters = arrayOf(inputFilter)

    }

    // 한국어 확인 함수
    fun checkKorean(editText: EditText) {
        Log.d(TAG, " - checkKorean")

        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            val regex = Regex("[ㄱ-ㅎ가-힣]+")
            if (source.toString().matches(regex)) {
                source
            } else {
                ""
            }
        }
        editText.filters = arrayOf(inputFilter)
    }

    // 숫자 + 11글자 확인 함수
    fun checkPhoneNumber(editText: EditText): Boolean {
        Log.d(TAG, " - checkPhoneNumber")

        val regex = Regex("[0-9]+")
        val isNumeric = editText.text.toString().matches(regex)
        val isElevenDigits = editText.text.toString().length == 11
        // 숫자로만 이루어졌는지 확인
        if (!isNumeric){
            editText.error = "숫자만 입력해주십시오."
        }
        // 11글자인지 확인
        if (!isElevenDigits){
            editText.error = "11자리를 입력해주십시오."
        }
        return isNumeric && isElevenDigits
    }
}