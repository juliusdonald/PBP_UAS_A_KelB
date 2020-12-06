package uts.uajy.kelompok_b_jualonline.api;

public class ReviewAPI {
    //    public static final String ROOT_URL   = "https://180709738.000webhostapp.com/";
    public static final String ROOT_URL   = "https://penjualanonline.apitubespbp.xyz/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/";

//    Route::post('review','Api\ReviewController@store'); // create
    //    method POST create
    public static final String URL_CREATE = ROOT_API + "review";

//    Route::get('review/{id}','Api\ReviewController@show'); //read selected by id
    //    method GET read selected by ID
    public static final String URL_GET_ID = ROOT_API + "review/";

//    Route::get('review','Api\ReviewController@index'); //read all
    //    method GET read all
    public static final String URL_GET = ROOT_API + "review";

//    Route::post('review/update/{id}','Api\ReviewController@update'); //update
    //    method POST update
    public static final String URL_UPDATE = ROOT_API + "review/update/";

//    Route::post('review/delete/{id}','Api\ReviewController@destroy'); //delete
    //    method POST delete
    public static final String URL_DELETE = ROOT_API + "review/delete/";
}
