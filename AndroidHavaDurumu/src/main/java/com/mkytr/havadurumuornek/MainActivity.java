package com.mkytr.havadurumuornek;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * MainActivity sınıfıdr. Uygulamamızda yer alan ve kullanıcıya gözüken tek Activity'nin sınıfıdır.
 *
 * @author Muhammed Kadir Yücel
 * @version 1.0
 * @since 07.09.2017
 */
public class MainActivity extends AppCompatActivity {
    private final int FINE_LOCATION_PERMISSION = 0;
    private  final int COARSE_LOCATION_PERMISSION = 1;
    private Location lastLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            Android 6.0'dan sonra zorunlu hale getirilen izin isteği kısmının gerçekleştirilmesi.
            Bu projede yalnızca konum bilgisine ihtiyaç duyulmuştur.
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
                Örneğin kullanıcı ayarlardan uygulama ayarlarına gitti ve konum iznini eliyle iptal
                etti. Bu durumda, uygulama tekrar açıldığında tekrar konum bilgisi için izin isteyecek
                ancak bu sefer konum bilgisinin neden istendiği de söylenecek.
             */
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                // Yalnızca kullanıcı izni eliyle iptal ettiğinde gösterilir
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Hava durumu için konum bilgisine ihtiyaç var");
                builder.setTitle("Konum bilgisi");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Kullanıcı diyalogtaki onay düğmesine basarsa izinleri tekrar iste
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);
                    }
                });
                AlertDialog locationInfoDialog = builder.create();
                locationInfoDialog.show();
            }else{
                // Eğer kullanıcıya konum bilgisinin neden istendiğinin gösterilmesine ihtiyaç yoksa
                // konum bilgisi izinlerini iste
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);
            }
        }else{
            // Hava durumunu yenile
            refreshWeather(null);
        }

    }

    /**
     * Android 6.0'dan sonra gerekli olan ve kullanıcı gizliliğinin korunması için gizlilik sıkıntısı
     * oluşabilecek bilgiler için kullanıcı izni gerekir. Konum bilgisi için de izin gereklidir.
     * İzin isteği cevabına göre programın farklı yöntemler izlemesi gerekebilir.
     *
     * @param requestCode İstenen izin ayrımı için gereklidir.
     * @param permissions İstek cevabıdır, izin verildi veya reddedildi.
     * @param grantResults İsteklerin sayısını tutar.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProgressBar pbMain = (ProgressBar) findViewById(R.id.pbMain);
        TextView tvHavaDurumu = (TextView) findViewById(R.id.tvHavaDurumu);

        switch(requestCode){
            case FINE_LOCATION_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    refreshWeather(null); // izin verildiyse sıcaklık bilgisini yenile
                }else{
                    // İzin verilmediyse
                    pbMain.setVisibility(View.GONE);
                    tvHavaDurumu.setVisibility(View.VISIBLE);
                    tvHavaDurumu.setText("Konum izni yok!");
                }
                return;
            case COARSE_LOCATION_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    refreshWeather(null); // izin verildiyse sıcaklık bilgisini yenile
                }else{
                    // İzin verilmediyse
                    pbMain.setVisibility(View.GONE);
                    tvHavaDurumu.setVisibility(View.VISIBLE);
                    tvHavaDurumu.setText("Konum izni yok!");
                }
                return;
        }
    }

    /**
     * Kullanıcı TextView üzerine dokunduğunda bu metod çağrılır ve hava durumu bilgisi tekrar
     * sorulur.
     * @param view View'dan onClick metodu için geçerlidir, null olarak çağrılabilir.
     */
    public void refreshWeather(View view){
        try{
            // Sistemden konum yöneticisini al
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // Kullanıcının son bilinen konumunu çağırır. Yani her hava durumu yenilendiğinde GPS
            // tekrar açılmaz. Cihazınız zaten belirli aralıklarla GPS'i açıp konum bilgisini yeniler
            // ve hafızada bir alana kayıt eder. Biz bu hafızadaki kayıt edilmiş son konum bilgisini
            // kullanarak hava durumunu yeniliyoruz. Eğer hafızada konum bilgisi yoksa GPS açılacak.
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }catch (SecurityException e){
            // Kullanıcı konum bilgisine izin vermemişse
            TextView tvHavaDurumu = (TextView) findViewById(R.id.tvHavaDurumu);
            tvHavaDurumu.setText("İzinsiz istek!");
            return;
        }catch (NullPointerException e){
            TextView tvHavaDurumu = (TextView) findViewById(R.id.tvHavaDurumu);
            tvHavaDurumu.setText("Dokunarak yenileyin.");
            return;
        }

        // Öncelikle internet bağlantısının olup olmadığına bakmamız gerekiyor
        // İnternet bağlantısı için Manifest içinde bazı izinleri istememiz gerekiyor
        // bkz. AndroidManifest.xml
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            // İnternet bağlantısı varsa AsyncTask oluştur ve çalıştır
            HavaDurumuGetir weatherTask = new HavaDurumuGetir(this, lastLocation.getLatitude(), lastLocation.getLongitude());
            weatherTask.execute();
        }else{
            // İnternet bağlantısı yoksa
            ProgressBar pbMain = (ProgressBar) findViewById(R.id.pbMain);
            TextView tvHavaDurumu = (TextView) findViewById(R.id.tvHavaDurumu);

            pbMain.setVisibility(View.GONE);
            tvHavaDurumu.setVisibility(View.VISIBLE);
            tvHavaDurumu.setText("Ağ bağlantısı yok!");
        }
    }

    /**
     * HavaDurumuGetir AsyncTask görevi bittiğinde bu metod çağrılarak arayüzde gerekli değişiklikler
     * yapılır. ProgressBar gizlenir, sıcaklık değeri TextView'e yazılır ve TextView görünür hale
     * getirilir.
     *
     * @param getCelcius API'den dönen Celcius cinsinde sıcaklık değeri.
     */
    public void showWeather(int getCelcius){
        ProgressBar pbMain = (ProgressBar) findViewById(R.id.pbMain);
        TextView tvHavaDurumu = (TextView) findViewById(R.id.tvHavaDurumu);

        String result = String.valueOf(getCelcius) + " °C";

        tvHavaDurumu.setText(result);
        pbMain.setVisibility(View.GONE);
        tvHavaDurumu.setVisibility(View.VISIBLE);
    }
}
