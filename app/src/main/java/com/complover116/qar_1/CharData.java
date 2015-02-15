package com.complover116.qar_1;

public class CharData {
public static final int W = 87;
public static final int A = 65;
public static final int S = 83;
public static final int D = 68;
public static final int Up = 38;
public static final int Left = 37;
public static final int Down = 40;
public static final int Right = 39; 
public static final int U = 85;
public static final int H = 72; 
public static final int J = 74;
public static final int K = 75;
public static final int Ast = 106;
public static final int Num8 = 104;
public static final int Num9 = 105;
public static final int NumPlus = 107;
public static int getTransformedButton(int initId) {
    if(CurGame.color == 1) {
        switch (initId) {
            case Up:
                return W;
            case Down:
                return S;
            case Left:
                return A;
            case Right:
                return D;
        }
    }
    if(CurGame.color == 2) {
        return initId;
    }
    if(CurGame.color == 3) {
        switch (initId) {
            case Up:
                return U;
            case Down:
                return J;
            case Left:
                return H;
            case Right:
                return K;
        }
    }
    if(CurGame.color == 4) {
        switch (initId) {
            case Up:
                return Ast;
            case Down:
                return Num9;
            case Left:
                return Num8;
            case Right:
                return NumPlus;
        }
    }
    return 228;
}
}
