package uts.uajy.kelompok_b_jualonline.api;

public class TransaksiAPI {

//    public static final String ROOT_URL   = "https://180709738.000webhostapp.com/";
    public static final String ROOT_URL   = "https://penjualanonline.apitubespbp.xyz/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/";

    //    Route::post('transaction','Api\TransaksiController@store'); // create
    //    method POST create
    public static final String URL_CREATE_TRANSACTION = ROOT_API + "transaction";

    //    Route::get('transaction/{id}','Api\TransaksiController@show'); //read selected by id
    //    method GET read selected by ID
    public static final String URL_GET_TRANSACTION_ID = ROOT_API + "transaction/";

    //    Route::get('transaction','Api\TransaksiController@index'); //read all
    //    method GET read all
    public static final String URL_GET_TRANSACTION = ROOT_API + "transaction";

    //    Route::post('transaction/update/{id}','Api\TransaksiController@update'); //update
    //    method POST update
    public static final String URL_UPDATE_TRANSACTION = ROOT_API + "transaction/update/";

    //    Route::post('transaction/delete/{id}','Api\TransaksiController@destroy'); //delete
    //    method POST delete
    public static final String URL_DELETE_TRANSACTION_ID = ROOT_API + "transaction/delete/";
}