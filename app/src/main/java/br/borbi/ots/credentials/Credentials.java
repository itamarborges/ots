package br.borbi.ots.credentials;

import br.borbi.ots.BuildConfig;

/**
 * Created by Gabriela on 01/05/2017.
 */

public final class Credentials {

    private Credentials() {
    }

    public static String getDeveloperForecast() {
        if (BuildConfig.DEBUG) {
            return (new Object() {
                int t;

                public String toString() {
                    byte[] buf = new byte[32];
                    t = -107070059;
                    buf[0] = (byte) (t >>> 2);
                    t = -1673610501;
                    buf[1] = (byte) (t >>> 23);
                    t = -1952610524;
                    buf[2] = (byte) (t >>> 5);
                    t = -1247687652;
                    buf[3] = (byte) (t >>> 6);
                    t = 2056883404;
                    buf[4] = (byte) (t >>> 11);
                    t = -1805239115;
                    buf[5] = (byte) (t >>> 17);
                    t = -407663056;
                    buf[6] = (byte) (t >>> 6);
                    t = 185765136;
                    buf[7] = (byte) (t >>> 6);
                    t = -1054667482;
                    buf[8] = (byte) (t >>> 12);
                    t = 1805233773;
                    buf[9] = (byte) (t >>> 11);
                    t = 1698961207;
                    buf[10] = (byte) (t >>> 24);
                    t = 477966733;
                    buf[11] = (byte) (t >>> 7);
                    t = -34082363;
                    buf[12] = (byte) (t >>> 3);
                    t = -945866484;
                    buf[13] = (byte) (t >>> 8);
                    t = -958760552;
                    buf[14] = (byte) (t >>> 2);
                    t = 675886389;
                    buf[15] = (byte) (t >>> 8);
                    t = -1220479900;
                    buf[16] = (byte) (t >>> 1);
                    t = -2075630319;
                    buf[17] = (byte) (t >>> 9);
                    t = 1117768759;
                    buf[18] = (byte) (t >>> 5);
                    t = 479199425;
                    buf[19] = (byte) (t >>> 23);
                    t = 1872355998;
                    buf[20] = (byte) (t >>> 15);
                    t = -1725137139;
                    buf[21] = (byte) (t >>> 13);
                    t = -430088144;
                    buf[22] = (byte) (t >>> 21);
                    t = -1147406916;
                    buf[23] = (byte) (t >>> 15);
                    t = -791424435;
                    buf[24] = (byte) (t >>> 18);
                    t = -833717211;
                    buf[25] = (byte) (t >>> 22);
                    t = 922078695;
                    buf[26] = (byte) (t >>> 6);
                    t = -1585856762;
                    buf[27] = (byte) (t >>> 11);
                    t = -1089719300;
                    buf[28] = (byte) (t >>> 8);
                    t = -1384539757;
                    buf[29] = (byte) (t >>> 11);
                    t = 459157311;
                    buf[30] = (byte) (t >>> 23);
                    t = -502122542;
                    buf[31] = (byte) (t >>> 8);
                    return new String(buf);
                }
            }.toString());
        }

        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[32];
                t = 743729534;
                buf[0] = (byte) (t >>> 21);
                t = 404663834;
                buf[1] = (byte) (t >>> 23);
                t = -151440957;
                buf[2] = (byte) (t >>> 8);
                t = 654693153;
                buf[3] = (byte) (t >>> 4);
                t = -971800843;
                buf[4] = (byte) (t >>> 21);
                t = -136898064;
                buf[5] = (byte) (t >>> 7);
                t = -1888458750;
                buf[6] = (byte) (t >>> 9);
                t = 506677444;
                buf[7] = (byte) (t >>> 16);
                t = 1538926223;
                buf[8] = (byte) (t >>> 5);
                t = -1917269933;
                buf[9] = (byte) (t >>> 22);
                t = 2060763919;
                buf[10] = (byte) (t >>> 9);
                t = 1676433777;
                buf[11] = (byte) (t >>> 13);
                t = -1283837032;
                buf[12] = (byte) (t >>> 4);
                t = 745747416;
                buf[13] = (byte) (t >>> 12);
                t = 873870451;
                buf[14] = (byte) (t >>> 24);
                t = 1623639448;
                buf[15] = (byte) (t >>> 13);
                t = 1609154035;
                buf[16] = (byte) (t >>> 11);
                t = -131740628;
                buf[17] = (byte) (t >>> 5);
                t = -230149624;
                buf[18] = (byte) (t >>> 8);
                t = 1721305910;
                buf[19] = (byte) (t >>> 3);
                t = -1504944236;
                buf[20] = (byte) (t >>> 20);
                t = -196283476;
                buf[21] = (byte) (t >>> 14);
                t = -1890511330;
                buf[22] = (byte) (t >>> 4);
                t = -614724005;
                buf[23] = (byte) (t >>> 6);
                t = -1596548321;
                buf[24] = (byte) (t >>> 3);
                t = 86803243;
                buf[25] = (byte) (t >>> 13);
                t = -2066213490;
                buf[26] = (byte) (t >>> 2);
                t = 1981590140;
                buf[27] = (byte) (t >>> 15);
                t = 958068775;
                buf[28] = (byte) (t >>> 24);
                t = 1502990256;
                buf[29] = (byte) (t >>> 19);
                t = 265603314;
                buf[30] = (byte) (t >>> 9);
                t = 1714015521;
                buf[31] = (byte) (t >>> 6);
                return new String(buf);
            }
        }.toString());
    }

    public static String getOpenWeather() {
        if (BuildConfig.DEBUG) {
            return (new Object() {
                int t;

                public String toString() {
                    byte[] buf = new byte[32];
                    t = 1799956153;
                    buf[0] = (byte) (t >>> 5);
                    t = -1942854278;
                    buf[1] = (byte) (t >>> 8);
                    t = -1674189423;
                    buf[2] = (byte) (t >>> 2);
                    t = 994652324;
                    buf[3] = (byte) (t >>> 8);
                    t = 925671015;
                    buf[4] = (byte) (t >>> 1);
                    t = 844164800;
                    buf[5] = (byte) (t >>> 24);
                    t = 1590340161;
                    buf[6] = (byte) (t >>> 17);
                    t = -336081885;
                    buf[7] = (byte) (t >>> 6);
                    t = 2052289330;
                    buf[8] = (byte) (t >>> 12);
                    t = -60623435;
                    buf[9] = (byte) (t >>> 17);
                    t = 320157151;
                    buf[10] = (byte) (t >>> 19);
                    t = 1298972774;
                    buf[11] = (byte) (t >>> 1);
                    t = 102762194;
                    buf[12] = (byte) (t >>> 20);
                    t = 1712029461;
                    buf[13] = (byte) (t >>> 21);
                    t = 246563794;
                    buf[14] = (byte) (t >>> 15);
                    t = 842023804;
                    buf[15] = (byte) (t >>> 24);
                    t = 812975187;
                    buf[16] = (byte) (t >>> 24);
                    t = -332651475;
                    buf[17] = (byte) (t >>> 13);
                    t = -1509084496;
                    buf[18] = (byte) (t >>> 7);
                    t = -2098151150;
                    buf[19] = (byte) (t >>> 10);
                    t = -2074279049;
                    buf[20] = (byte) (t >>> 4);
                    t = 1942383198;
                    buf[21] = (byte) (t >>> 9);
                    t = 854383428;
                    buf[22] = (byte) (t >>> 13);
                    t = 16082657;
                    buf[23] = (byte) (t >>> 8);
                    t = -387728399;
                    buf[24] = (byte) (t >>> 18);
                    t = 1503221150;
                    buf[25] = (byte) (t >>> 18);
                    t = -140756468;
                    buf[26] = (byte) (t >>> 15);
                    t = 836776346;
                    buf[27] = (byte) (t >>> 23);
                    t = 808930986;
                    buf[28] = (byte) (t >>> 16);
                    t = 1347198710;
                    buf[29] = (byte) (t >>> 14);
                    t = 1936923323;
                    buf[30] = (byte) (t >>> 12);
                    t = 1532078655;
                    buf[31] = (byte) (t >>> 11);
                    return new String(buf);
                }
            }.toString());
        }
        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[32];
                t = 631108478;
                buf[0] = (byte) (t >>> 4);
                t = 639887392;
                buf[1] = (byte) (t >>> 21);
                t = 1734646076;
                buf[2] = (byte) (t >>> 7);
                t = 1676882840;
                buf[3] = (byte) (t >>> 12);
                t = -1818598133;
                buf[4] = (byte) (t >>> 9);
                t = -1405186738;
                buf[5] = (byte) (t >>> 6);
                t = -1683503597;
                buf[6] = (byte) (t >>> 23);
                t = 925705829;
                buf[7] = (byte) (t >>> 5);
                t = 1379061814;
                buf[8] = (byte) (t >>> 16);
                t = -778555065;
                buf[9] = (byte) (t >>> 19);
                t = -1113119019;
                buf[10] = (byte) (t >>> 13);
                t = -267812827;
                buf[11] = (byte) (t >>> 11);
                t = 124462705;
                buf[12] = (byte) (t >>> 11);
                t = 210091325;
                buf[13] = (byte) (t >>> 21);
                t = -1482342805;
                buf[14] = (byte) (t >>> 1);
                t = -1632102;
                buf[15] = (byte) (t >>> 13);
                t = -664583181;
                buf[16] = (byte) (t >>> 16);
                t = 226719521;
                buf[17] = (byte) (t >>> 22);
                t = -1293516595;
                buf[18] = (byte) (t >>> 23);
                t = -563993719;
                buf[19] = (byte) (t >>> 16);
                t = 942751638;
                buf[20] = (byte) (t >>> 16);
                t = 1281202091;
                buf[21] = (byte) (t >>> 10);
                t = 724274136;
                buf[22] = (byte) (t >>> 19);
                t = -1736143365;
                buf[23] = (byte) (t >>> 22);
                t = -882145768;
                buf[24] = (byte) (t >>> 4);
                t = 543300298;
                buf[25] = (byte) (t >>> 1);
                t = 1432459639;
                buf[26] = (byte) (t >>> 11);
                t = 966070525;
                buf[27] = (byte) (t >>> 19);
                t = -2095325600;
                buf[28] = (byte) (t >>> 1);
                t = -1201344799;
                buf[29] = (byte) (t >>> 17);
                t = 456895877;
                buf[30] = (byte) (t >>> 2);
                t = 858172960;
                buf[31] = (byte) (t >>> 20);
                return new String(buf);
            }
        }.toString());
    }
}
