package uts.uajy.kelompok_b_jualonline.api;

public class BarangAPI {

//    public static final String ROOT_URL   = "https://180709738.000webhostapp.com/";
    public static final String ROOT_URL   = "https://penjualanonline.apitubespbp.xyz/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/";

    //    Route::post('barang','Api\BarangController@store'); // create
    //    method POST create
    public static final String URL_CREATE = ROOT_API + "barang";

    //    Route::get('barang/{id}','Api\BarangController@show'); //read selected by id
    //    method GET read selected by ID
    public static final String URL_GET_ID = ROOT_API + "barang/";

    //    Route::get('barang','Api\BarangController@index'); //read all
    //    method GET read all
    public static final String URL_GET = ROOT_API + "barang";

    //    Route::post('barang/update/{id}','Api\BarangController@update'); //update
    //    method POST update
    public static final String URL_UPDATE = ROOT_API + "barang/update/";

    //    Route::post('barang/delete/{id}','Api\BarangController@destroy'); //delete
    //    method POST delete
    public static final String URL_DELETE = ROOT_API + "barang/delete/";
}
