import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_record -> {
                    // 기록 화면으로 이동
                    true
                }
                R.id.menu_home -> {
                    // 홈 화면으로 이동
                    true
                }
                R.id.menu_statistics -> {
                    // 통계 화면으로 이동
                    true
                }
                else -> false
            }
        }
    }
}
// 연습ㅎ--