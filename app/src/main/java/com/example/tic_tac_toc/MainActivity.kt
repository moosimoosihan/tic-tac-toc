package com.example.tic_tac_toc

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.tic_tac_toc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMainBinding

    //버튼 객체 담을 변수
    private val buttons = arrayOfNulls<Button>(9)

    //활성화 플레이어(true: p1 , false : p2)
    private var activityPlayer: Boolean = true

    private var p1ScoreCount = 0 //플레이어1 승수
    private var p2ScoreCount = 0 //플레이어2 승수
    private var roundCount = 0 // 클릭 갯수

    //클릭 한 버튼위치 들어갈 배열
    var gameState: IntArray = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)

    //승리 위치
    var winningPosition: Array<IntArray> =
        arrayOf<IntArray>(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //순서 표시
        viewMode()

        //버튼 초기화
        for((index, item) in buttons.withIndex()){

            val buttonID = "btn_${index}"
            val resourceID = resources.getIdentifier(buttonID, "id", packageName)
            buttons[index] = findViewById(resourceID)
            buttons[index]?.setOnClickListener(this)
        }

        //초기화 버튼
        binding.resetBtn.setOnClickListener {

            //게임 초기화
            playAgain()

            p1ScoreCount = 0
            p2ScoreCount = 0

            //승수 업데이트
            updatePlayerScore()
        }
    }//onCreate

    /**
     * 버튼 클릭 이벤트
     */
    override fun onClick(view: View?) {

        //이미 값이 있으면 리턴
        if((view as Button).text.toString() != ""){

            Toast.makeText(applicationContext, "이미 체크됨", Toast.LENGTH_SHORT).show()
            return
        }

        //1. 버튼 ID 변수에 담기(예: btn_2)
        val buttonId = view.resources.getResourceEntryName(view.id)

        //2. 버튼 ID에서 숫자만 변수에 담기(예: 2)
        val gameStatePointer = buttonId.substring(buttonId.length-1, buttonId.length).toInt()

        //사용자 순서
        if((activityPlayer)){ //플레이어 1

            //텍스트 설정
            view.text = "X"

            //텍스트 색상 설정
            view.setTextColor(Color.parseColor("#FFC34A"))

            //해당 위치에 0을 넣는다.
            gameState[gameStatePointer] = 0

        }else{ //플레이어2
            //텍스트 설정
            view.text = "O"

            //텍스트 색상 설정
            view.setTextColor(Color.parseColor("#70FFEA"))

            //해당 위치에 0을 넣는다.
            gameState[gameStatePointer] = 1
        }

        roundCount++ //턴 수 올리기기

        //승리 체크
        if(checkWinner()){
            if(activityPlayer){//플레이어1이 승리
                p1ScoreCount++ //승수올리기
                updatePlayerScore() //승수 업데이트
                Toast.makeText(this, "플레이어1 승리", Toast.LENGTH_SHORT).show()
                playAgain()//게임 초기화

            }else{//플레이어2 승리
                p2ScoreCount++ //승수올리기
                updatePlayerScore() //승수 업데이트
                Toast.makeText(this, "플레이어2 승리", Toast.LENGTH_SHORT).show()
                playAgain()//게임 초기화
            }
        }else if(roundCount == 9){ //클릭 횟수 9개 채웠는데 승부가 안나면
            playAgain()//초기화
            Toast.makeText(this, "무승부", Toast.LENGTH_SHORT).show()
        }else{ //계속 게임
            //순서 변경
            activityPlayer = !activityPlayer
        }

        //순서 표시
        viewMode()
    }

    /**
     * 순서 표시
     */
    private fun viewMode(){

        if(activityPlayer){
            binding.player1.setTextColor(Color.RED)
            binding.player2.setTextColor(Color.BLACK)
        }else{
            binding.player1.setTextColor(Color.BLACK)
            binding.player2.setTextColor(Color.RED)
        }
    }

    /**
     * 게임 초기화
     */
    private fun playAgain(){
        roundCount = 0 //클릭 횟수
        activityPlayer = true // 플레이어 순서

        //버튼 초기화
        for((index, item) in buttons.withIndex()){
            gameState[index] = 2
            buttons[index]?.text = ""
        }
    }

    /**
     * 승수 업데이트
     */
    private fun updatePlayerScore(){
        //화면에 승수 적용
        binding.player1Score.text = p1ScoreCount.toString()
        binding.player2Score.text = p2ScoreCount.toString()
    }

    /**
     * 승리 체크
     */
    private fun checkWinner(): Boolean{
        //결과
        var winnerResult = false

        for(winPosition in winningPosition){
            //승리위치 비교
            if(gameState[winPosition[0]] == gameState[winPosition[1]] &&
                gameState[winPosition[1]] == gameState[winPosition[2]] &&
                gameState[winPosition[0]] != 2){

                winnerResult = true
            }
        }
        return winnerResult
    }
}