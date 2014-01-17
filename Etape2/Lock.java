package Etape2;

public enum Lock {

		 NL, // no local lock
		 RLC,// read lock cached (not taken)
		 WLC,// write lock cached
		 RLT,// read lock taken
		 WLT,// write lock taken
		 RLT_WLC// read lock taken and write lock cached
}
