package br.borbi.ots.utility;

import br.borbi.ots.data.OTSContract;

/**
 * Created by Gabriela on 15/06/2015.
 */
class QueryUtility {

    public static String buildQuerySelectSearchByCoordinatesAndDate(){
        String whereClause = OTSContract.Search.COLUMN_NAME_ORIGIN_LAT +
                " >= ?" +
                " AND " +
                OTSContract.Search.COLUMN_NAME_ORIGIN_LAT + " <= ?" +
                " AND " +
                OTSContract.Search.COLUMN_NAME_ORIGIN_LONG + " >= ?" +
                " AND " +
                OTSContract.Search.COLUMN_NAME_ORIGIN_LONG + " <= ?" +
                " AND " +
                OTSContract.Search.COLUMN_NAME_DATE_END + " >= ?";

        return whereClause;
    }

    public static String buildQuerySelectSearchByDate(){
        return OTSContract.Search.COLUMN_NAME_DATE_END + " >= ?";
    }
}
