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

    public static String getAmazon() {
        if (BuildConfig.DEBUG) {
            return (new Object() {
                int t;

                public String toString() {
                    byte[] buf = new byte[21];
                    t = -633742084;
                    buf[0] = (byte) (t >>> 6);
                    t = -154954288;
                    buf[1] = (byte) (t >>> 17);
                    t = -355149033;
                    buf[2] = (byte) (t >>> 9);
                    t = 1305442754;
                    buf[3] = (byte) (t >>> 2);
                    t = -321913128;
                    buf[4] = (byte) (t >>> 1);
                    t = 187483231;
                    buf[5] = (byte) (t >>> 19);
                    t = -2105141524;
                    buf[6] = (byte) (t >>> 7);
                    t = 1638099570;
                    buf[7] = (byte) (t >>> 24);
                    t = -1283339895;
                    buf[8] = (byte) (t >>> 19);
                    t = -602292194;
                    buf[9] = (byte) (t >>> 22);
                    t = 852844403;
                    buf[10] = (byte) (t >>> 20);
                    t = -1758470753;
                    buf[11] = (byte) (t >>> 6);
                    t = 639246730;
                    buf[12] = (byte) (t >>> 3);
                    t = -223350142;
                    buf[13] = (byte) (t >>> 15);
                    t = -1998498214;
                    buf[14] = (byte) (t >>> 17);
                    t = -1609706446;
                    buf[15] = (byte) (t >>> 10);
                    t = -2075368940;
                    buf[16] = (byte) (t >>> 8);
                    t = -1311467823;
                    buf[17] = (byte) (t >>> 7);
                    t = -763193024;
                    buf[18] = (byte) (t >>> 7);
                    t = 163646508;
                    buf[19] = (byte) (t >>> 5);
                    t = 1751210386;
                    buf[20] = (byte) (t >>> 16);
                    return new String(buf);
                }
            }.toString());
        }

        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[32];
                t = 789710021;
                buf[0] = (byte) (t >>> 1);
                t = -1476932369;
                buf[1] = (byte) (t >>> 6);
                t = -107377965;
                buf[2] = (byte) (t >>> 15);
                t = 563689671;
                buf[3] = (byte) (t >>> 2);
                t = -617496648;
                buf[4] = (byte) (t >>> 11);
                t = -960720780;
                buf[5] = (byte) (t >>> 21);
                t = 1042475398;
                buf[6] = (byte) (t >>> 3);
                t = 898217949;
                buf[7] = (byte) (t >>> 18);
                t = 814014070;
                buf[8] = (byte) (t >>> 24);
                t = -1942901046;
                buf[9] = (byte) (t >>> 1);
                t = 1120125925;
                buf[10] = (byte) (t >>> 17);
                t = -113945591;
                buf[11] = (byte) (t >>> 16);
                t = 886238919;
                buf[12] = (byte) (t >>> 18);
                t = 791127634;
                buf[13] = (byte) (t >>> 4);
                t = 508971291;
                buf[14] = (byte) (t >>> 12);
                t = -859295048;
                buf[15] = (byte) (t >>> 21);
                t = -401063648;
                buf[16] = (byte) (t >>> 14);
                t = -1204623743;
                buf[17] = (byte) (t >>> 15);
                t = 885581319;
                buf[18] = (byte) (t >>> 17);
                t = -1299929575;
                buf[19] = (byte) (t >>> 4);
                t = -1536500759;
                buf[20] = (byte) (t >>> 17);
                t = 475798563;
                buf[21] = (byte) (t >>> 23);
                t = -518873990;
                buf[22] = (byte) (t >>> 7);
                t = 1574329648;
                buf[23] = (byte) (t >>> 12);
                t = -27060737;
                buf[24] = (byte) (t >>> 11);
                t = -1117133292;
                buf[25] = (byte) (t >>> 4);
                t = -1734712323;
                buf[26] = (byte) (t >>> 15);
                t = -2087688683;
                buf[27] = (byte) (t >>> 20);
                t = -1703958222;
                buf[28] = (byte) (t >>> 23);
                t = -215118939;
                buf[29] = (byte) (t >>> 20);
                t = 58264393;
                buf[30] = (byte) (t >>> 20);
                t = -858814285;
                buf[31] = (byte) (t >>> 21);
                return new String(buf);
            }
        }.toString());
    }

    public static String getAdMob() {
        if (BuildConfig.DEBUG) {
            return (new Object() {
                int t;

                public String toString() {
                    byte[] buf = new byte[38];
                    t = -691758855;
                    buf[0] = (byte) (t >>> 6);
                    t = -374297417;
                    buf[1] = (byte) (t >>> 15);
                    t = 756020450;
                    buf[2] = (byte) (t >>> 24);
                    t = -1325981449;
                    buf[3] = (byte) (t >>> 23);
                    t = 997239773;
                    buf[4] = (byte) (t >>> 16);
                    t = -1910785566;
                    buf[5] = (byte) (t >>> 21);
                    t = -477964789;
                    buf[6] = (byte) (t >>> 12);
                    t = -515669479;
                    buf[7] = (byte) (t >>> 11);
                    t = 1952806188;
                    buf[8] = (byte) (t >>> 8);
                    t = -1270069149;
                    buf[9] = (byte) (t >>> 13);
                    t = -1956003338;
                    buf[10] = (byte) (t >>> 22);
                    t = 359904057;
                    buf[11] = (byte) (t >>> 4);
                    t = 484951079;
                    buf[12] = (byte) (t >>> 23);
                    t = 197346277;
                    buf[13] = (byte) (t >>> 12);
                    t = 806595549;
                    buf[14] = (byte) (t >>> 24);
                    t = 103994923;
                    buf[15] = (byte) (t >>> 16);
                    t = -986356443;
                    buf[16] = (byte) (t >>> 16);
                    t = 1304872646;
                    buf[17] = (byte) (t >>> 13);
                    t = -402254127;
                    buf[18] = (byte) (t >>> 13);
                    t = -1819066761;
                    buf[19] = (byte) (t >>> 20);
                    t = -1189314163;
                    buf[20] = (byte) (t >>> 15);
                    t = 1507023987;
                    buf[21] = (byte) (t >>> 1);
                    t = -2092647372;
                    buf[22] = (byte) (t >>> 20);
                    t = -751444891;
                    buf[23] = (byte) (t >>> 1);
                    t = 1731027375;
                    buf[24] = (byte) (t >>> 3);
                    t = 1724134566;
                    buf[25] = (byte) (t >>> 8);
                    t = -1823636929;
                    buf[26] = (byte) (t >>> 20);
                    t = -1905250396;
                    buf[27] = (byte) (t >>> 8);
                    t = -479792468;
                    buf[28] = (byte) (t >>> 20);
                    t = 1768426725;
                    buf[29] = (byte) (t >>> 6);
                    t = 240241725;
                    buf[30] = (byte) (t >>> 6);
                    t = 1452030490;
                    buf[31] = (byte) (t >>> 14);
                    t = 1730803738;
                    buf[32] = (byte) (t >>> 21);
                    t = -1683649081;
                    buf[33] = (byte) (t >>> 6);
                    t = -835329970;
                    buf[34] = (byte) (t >>> 22);
                    t = -1506696075;
                    buf[35] = (byte) (t >>> 16);
                    t = 94422881;
                    buf[36] = (byte) (t >>> 10);
                    t = -1872181523;
                    buf[37] = (byte) (t >>> 10);
                    return new String(buf);
                }
            }.toString());
        }

        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[38];
                t = -1501357816;
                buf[0] = (byte) (t >>> 11);
                t = -2124304760;
                buf[1] = (byte) (t >>> 16);
                t = 1957392745;
                buf[2] = (byte) (t >>> 3);
                t = 2138770823;
                buf[3] = (byte) (t >>> 11);
                t = 773658686;
                buf[4] = (byte) (t >>> 21);
                t = 400978099;
                buf[5] = (byte) (t >>> 8);
                t = -19304553;
                buf[6] = (byte) (t >>> 11);
                t = 189191949;
                buf[7] = (byte) (t >>> 4);
                t = 944377523;
                buf[8] = (byte) (t >>> 5);
                t = 1667424736;
                buf[9] = (byte) (t >>> 16);
                t = -651362034;
                buf[10] = (byte) (t >>> 16);
                t = 1963253567;
                buf[11] = (byte) (t >>> 10);
                t = -1038377925;
                buf[12] = (byte) (t >>> 15);
                t = 831627785;
                buf[13] = (byte) (t >>> 19);
                t = -1136498909;
                buf[14] = (byte) (t >>> 9);
                t = -1161591100;
                buf[15] = (byte) (t >>> 12);
                t = 839801901;
                buf[16] = (byte) (t >>> 14);
                t = -1909896182;
                buf[17] = (byte) (t >>> 22);
                t = -1664831649;
                buf[18] = (byte) (t >>> 23);
                t = -386889939;
                buf[19] = (byte) (t >>> 5);
                t = 906055784;
                buf[20] = (byte) (t >>> 1);
                t = -1097000543;
                buf[21] = (byte) (t >>> 7);
                t = 1921915727;
                buf[22] = (byte) (t >>> 14);
                t = -1315180005;
                buf[23] = (byte) (t >>> 15);
                t = -63751840;
                buf[24] = (byte) (t >>> 16);
                t = -895850379;
                buf[25] = (byte) (t >>> 9);
                t = -202785531;
                buf[26] = (byte) (t >>> 11);
                t = -580367266;
                buf[27] = (byte) (t >>> 1);
                t = 1397312930;
                buf[28] = (byte) (t >>> 6);
                t = 1975117516;
                buf[29] = (byte) (t >>> 5);
                t = 205432468;
                buf[30] = (byte) (t >>> 5);
                t = -1026618998;
                buf[31] = (byte) (t >>> 3);
                t = -1613908749;
                buf[32] = (byte) (t >>> 14);
                t = -87476526;
                buf[33] = (byte) (t >>> 8);
                t = 900091799;
                buf[34] = (byte) (t >>> 13);
                t = 413470963;
                buf[35] = (byte) (t >>> 23);
                t = -616228707;
                buf[36] = (byte) (t >>> 7);
                t = -1242504423;
                buf[37] = (byte) (t >>> 10);
                return new String(buf);
            }
        }.toString());
    }

    public static String getAnalytics() {
        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[13];
                t = 1722546525;
                buf[0] = (byte) (t >>> 17);
                t = 1306821529;
                buf[1] = (byte) (t >>> 9);
                t = 1121544384;
                buf[2] = (byte) (t >>> 11);
                t = 2099471344;
                buf[3] = (byte) (t >>> 12);
                t = 363964678;
                buf[4] = (byte) (t >>> 11);
                t = -1367814635;
                buf[5] = (byte) (t >>> 6);
                t = -1081972536;
                buf[6] = (byte) (t >>> 9);
                t = 1940283543;
                buf[7] = (byte) (t >>> 13);
                t = -1764552763;
                buf[8] = (byte) (t >>> 12);
                t = 371507822;
                buf[9] = (byte) (t >>> 10);
                t = 1662989372;
                buf[10] = (byte) (t >>> 20);
                t = 1521699700;
                buf[11] = (byte) (t >>> 6);
                t = -1507776131;
                buf[12] = (byte) (t >>> 21);
                return new String(buf);
            }
        }.toString());
    }


    public static String getAdMobInterstitial() {
//        if (BuildConfig.DEBUG) {
//            return (new Object() {
//                int t;
//
//                public String toString() {
//                    byte[] buf = new byte[38];
//                    t = 1260385355;
//                    buf[0] = (byte) (t >>> 19);
//                    t = -1115379230;
//                    buf[1] = (byte) (t >>> 18);
//                    t = -1452956129;
//                    buf[2] = (byte) (t >>> 13);
//                    t = 1928006100;
//                    buf[3] = (byte) (t >>> 11);
//                    t = 1442776515;
//                    buf[4] = (byte) (t >>> 2);
//                    t = 1318198313;
//                    buf[5] = (byte) (t >>> 6);
//                    t = 177780697;
//                    buf[6] = (byte) (t >>> 10);
//                    t = -1676790726;
//                    buf[7] = (byte) (t >>> 22);
//                    t = -1755994209;
//                    buf[8] = (byte) (t >>> 20);
//                    t = -120908342;
//                    buf[9] = (byte) (t >>> 11);
//                    t = -1956761697;
//                    buf[10] = (byte) (t >>> 22);
//                    t = 1355803955;
//                    buf[11] = (byte) (t >>> 18);
//                    t = -600241872;
//                    buf[12] = (byte) (t >>> 16);
//                    t = 875185505;
//                    buf[13] = (byte) (t >>> 24);
//                    t = 1276166510;
//                    buf[14] = (byte) (t >>> 22);
//                    t = -1296684856;
//                    buf[15] = (byte) (t >>> 2);
//                    t = -631045288;
//                    buf[16] = (byte) (t >>> 4);
//                    t = -1855768959;
//                    buf[17] = (byte) (t >>> 8);
//                    t = -1526528169;
//                    buf[18] = (byte) (t >>> 12);
//                    t = -1661137367;
//                    buf[19] = (byte) (t >>> 23);
//                    t = -482646809;
//                    buf[20] = (byte) (t >>> 2);
//                    t = -1194441812;
//                    buf[21] = (byte) (t >>> 14);
//                    t = 2087294978;
//                    buf[22] = (byte) (t >>> 17);
//                    t = 1170367951;
//                    buf[23] = (byte) (t >>> 9);
//                    t = 2072746641;
//                    buf[24] = (byte) (t >>> 7);
//                    t = 195757672;
//                    buf[25] = (byte) (t >>> 1);
//                    t = 1772199206;
//                    buf[26] = (byte) (t >>> 19);
//                    t = 1121960698;
//                    buf[27] = (byte) (t >>> 4);
//                    t = 1595452245;
//                    buf[28] = (byte) (t >>> 15);
//                    t = -1422911874;
//                    buf[29] = (byte) (t >>> 16);
//                    t = 859529746;
//                    buf[30] = (byte) (t >>> 24);
//                    t = 1503171809;
//                    buf[31] = (byte) (t >>> 19);
//                    t = 578078087;
//                    buf[32] = (byte) (t >>> 10);
//                    t = 461476594;
//                    buf[33] = (byte) (t >>> 23);
//                    t = 660070;
//                    buf[34] = (byte) (t >>> 1);
//                    t = 494492271;
//                    buf[35] = (byte) (t >>> 1);
//                    t = 587252739;
//                    buf[36] = (byte) (t >>> 10);
//                    t = 855047692;
//                    buf[37] = (byte) (t >>> 24);
//                    return new String(buf);
//                }
//            }.toString());
//        }
        return (new Object() {
            int t;

            public String toString() {
                byte[] buf = new byte[38];
                t = -1536994642;
                buf[0] = (byte) (t >>> 16);
                t = -1022413380;
                buf[1] = (byte) (t >>> 19);
                t = -2054580523;
                buf[2] = (byte) (t >>> 7);
                t = 1641746990;
                buf[3] = (byte) (t >>> 24);
                t = 339851294;
                buf[4] = (byte) (t >>> 7);
                t = -1659665314;
                buf[5] = (byte) (t >>> 11);
                t = -1036864805;
                buf[6] = (byte) (t >>> 4);
                t = 545841442;
                buf[7] = (byte) (t >>> 9);
                t = -649351749;
                buf[8] = (byte) (t >>> 11);
                t = 401806306;
                buf[9] = (byte) (t >>> 11);
                t = -1529128598;
                buf[10] = (byte) (t >>> 3);
                t = 932776553;
                buf[11] = (byte) (t >>> 24);
                t = -2030438020;
                buf[12] = (byte) (t >>> 21);
                t = 420686335;
                buf[13] = (byte) (t >>> 23);
                t = -1901736721;
                buf[14] = (byte) (t >>> 6);
                t = 874629047;
                buf[15] = (byte) (t >>> 11);
                t = 1827140047;
                buf[16] = (byte) (t >>> 3);
                t = -1671463420;
                buf[17] = (byte) (t >>> 23);
                t = 30048118;
                buf[18] = (byte) (t >>> 19);
                t = 957075858;
                buf[19] = (byte) (t >>> 24);
                t = 882371732;
                buf[20] = (byte) (t >>> 24);
                t = 1753272147;
                buf[21] = (byte) (t >>> 10);
                t = -1004783709;
                buf[22] = (byte) (t >>> 15);
                t = -523182121;
                buf[23] = (byte) (t >>> 10);
                t = 2134109983;
                buf[24] = (byte) (t >>> 16);
                t = 1792844192;
                buf[25] = (byte) (t >>> 3);
                t = 468511285;
                buf[26] = (byte) (t >>> 23);
                t = 657196533;
                buf[27] = (byte) (t >>> 5);
                t = -1153935174;
                buf[28] = (byte) (t >>> 16);
                t = -672687434;
                buf[29] = (byte) (t >>> 7);
                t = -1536041770;
                buf[30] = (byte) (t >>> 2);
                t = -1803975088;
                buf[31] = (byte) (t >>> 5);
                t = -1214196448;
                buf[32] = (byte) (t >>> 10);
                t = 114376306;
                buf[33] = (byte) (t >>> 21);
                t = -1124323324;
                buf[34] = (byte) (t >>> 8);
                t = -1270013544;
                buf[35] = (byte) (t >>> 3);
                t = -1919406332;
                buf[36] = (byte) (t >>> 22);
                t = 202965782;
                buf[37] = (byte) (t >>> 4);
                return new String(buf);
            }
        }.toString());
    }
}
