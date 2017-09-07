package com.mkytr.havadurumuornek;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HavaDurumuGetir bir AsyncTask'dır ve arayüzü dondurmadan arkaplanda OpenWeatherMap API'si üzerinden
 * sıcaklık bilgisini çekmek için tasarlanmıştır.
 *
 * @author Muhammed Kadir Yücel
 * @version 1.0
 * @since 07.09.2017
 */
public class HavaDurumuGetir extends AsyncTask<String, Void, String> {

    private final String apiKey = "API_KEYINIZ"; // TODO: OpenWeatherMap API key buraya
    private String reqUrl = ""; // Constructor tarafından oluşturulacak sorgu URL'si
    private Context context;

    /**
     * Oluşturucu metoddur (Constructor), AsyncTask'ı başlatmak için gerekli olan
     * paremetrelerin alınması içindir.
     *
     * @param getContext Aktiviteyi alır, aktiviteden metod çağırmak içindir.
     * @param lat Konumun latitude bilgisi
     * @param lon Konumun longitude bilgisi
     */
    public HavaDurumuGetir(Context getContext, double lat, double lon){
        this.context = getContext;
        reqUrl = "http://api.openweathermap.org/data/2.5/weather?lat="
                + String.valueOf(lat)
                +"&lon="+ String.valueOf(lon)
                +"&appid=" + apiKey;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String response = null;
        try{
            // Stringden URL'e dönüştürülecek, başarısız olursa MalformedURLException oluşacak
            URL url = new URL(reqUrl);

            // URL ile HTTP bağlantısı oluşturma
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // GET sorgusu gönderiyoruz
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            StringBuilder stringBuilder = new StringBuilder();

            // Gelen cevabı satır satır oku ve bir String içinde tut
            String line;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append('\n');
            }

            try{
                // Bağlantıyı kapatmamız gerekiyor
                inStream.close();
            }catch (IOException e){
                // Bağlantı kapatma sırasında hata oluşursa
                Log.e("IOException", "Weather Task, inner IO exception");
            }
            response = stringBuilder.toString();
        }catch(MalformedURLException e){
            // URL hatalıysa
            Log.e("URLException", "Weather Task, malformed URL exception!");
        }catch(IOException e){
            // Bağlantı sırasında okuma hatası olursa
            Log.e("IOException", "Weather Task, IO exception");
        }

        return response; // String onPostExecute'a gönderiliyor
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // Activity'i al, içideki metodları erişmek için gerekli
        MainActivity mainActivity = (MainActivity) context;

        if(s != null){ // HTTP'den boş olmayan bir dönüş varsa
            try{
                /*
                    Dönen JSON verisinin okunması, tamamen dönen JSON yapısına bağlıdır. Yukarıda
                    oluşan reqURL'i bir tarayıcıda açın ve dönen JSON tipini görebilirsiniz.

                    Örnek: http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b1b15e88fa797225412429c1c50c122a1
                 */

                // Tüm içerik bütün bir JSON obje içinde
                JSONObject jsonObject = new JSONObject(s);

                // Sıcaklık bilgisi bütün obje içinde yer alan main isimli objenin içinde
                JSONObject mainObject = jsonObject.getJSONObject("main");

                // Sıcaklık verisi main objesi içinde temp değer alanı içerisinde yer alıyor
                // OpenWeatherMap Kelvin tipinde sıcaklık döndürüyor
                // Celcius'a çevirmek gerekiyor
                Double kelvinValue = mainObject.getDouble("temp") - 273.15;
                int celciusValue =  kelvinValue.intValue();

                // Activity'den gerekli metodu çağırıyoruz
                mainActivity.showWeather(celciusValue);
            }catch (JSONException e){
                // JSON yapısı bozuksa
                Log.e("JSONException", "Weather Task, JSON Exception");
                mainActivity.showWeather(0);
            }
        }else{
            // Gerekli bilgi alınamazsa derece olarak 0 döndürülecek
            // Farklı yöntemler izlenebilir
            mainActivity.showWeather(0);
        }
    }
}
