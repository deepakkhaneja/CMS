
class ID {
	int t1;
	char t2 = 0;
	int t3 = 0;
	int t4 = 0;
	int IDtype;
	
	public ID(int t1) { //'D' = deanID
		IDtype = 1;
		this.t1 = t1;
	}
	
	public static ID[] sort(ID arrayID[]){
		int n = arrayID.length;
		int maxIndex;
		ID maxID;
		for(int i = n - 1; i > 0; i--) {
			maxIndex = 0; maxID = arrayID[0];
			for(int j = 0; j <= i; j++)
				if(arrayID[j].wt() > maxID.wt()) {
					maxIndex = j;
					maxID = arrayID[j];
				}
			arrayID[maxIndex] = arrayID[i];
			arrayID[i] = maxID;
		}
		return arrayID; 
	} 
	
	public static ID getMax(ID arrayID[]){
		int n = arrayID.length;
		ID maxID;
		maxID = new ID("0");
		for(int j = 0; j <= n-1; j++)
			if(arrayID[j].wt() > maxID.wt())
				maxID = arrayID[j];
		if(n == 0) return null; else return maxID; 
	} 

	public int wt() {
		return 10000000 * getT1() + 10000 * (int)getT2() + 100 * getT3() + getT4(); 
	}

	public ID(int t1, char t2, int t3) { //sem = 1 or 2, deptt = 'A', 'B'.., courseNo = serialNo
		IDtype = 3;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	
	public ID(int t1, char t2, int t3, int t4) { //year = 12, 13,...,  deptt = 'A', 'B'.., deg = 1, 2, studentNo = serialNo
		IDtype = 4;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		this.t4 = t4;
	}
	
	public ID(String str) {
		char arrayID[] = str.toCharArray();
		if(arrayID.length == 1) {
			IDtype = 1;
			this.t1 = arrayID[0];
		} else if(arrayID[1] < '0' || arrayID[1] > '9') {
			IDtype = 3;
			this.t1 = arrayID[0];
			this.t2 = arrayID[1];
			this.t3 = (new Integer(new String(arrayID, 2, arrayID.length - 2))).intValue();
		} else {
			IDtype = 4;
			this.t1 = (new Integer(new String(arrayID, 0, 2))).intValue();
			this.t2 = arrayID[2];
			this.t3 = (new Integer(new String(""+arrayID[3]))).intValue();
			this.t4 = (new Integer(new String(arrayID, 4, arrayID.length - 4))).intValue();
		}
	}

	public static boolean isCourseID(String courseID) {
		char arrayID[] = courseID.toCharArray();
		if(arrayID.length < 3) return false;
		if(arrayID[0] != '1' && arrayID[0] != '2') return false;
		boolean b = false;
		for(int i = 0; i < Student.TOTAL_DEPTT; i++)
			if(arrayID[1] == 'A' + i) b = true;
		if(!b) return false;
		if(arrayID[2] == '0') return false;
		for(int k = 2; k < arrayID.length; k++) {
			if(arrayID[k] < '0' || arrayID[k] > '9') return false;
		}
		return true;
	}	
	
	public int getT1() {
		return t1;
	}


	public char getT2() {
		return t2;
	}

	public int getT3() {
		return t3;
	}

	public int getT4() {
		return t4;
	}

	public String toString() {
		if(IDtype == 1) return ("" + (char)t1);
		else if(IDtype == 3) return ("" + (char)t1 + t2 + t3);
		else return ("" + t1 + t2 + t3 + t4);
	}
	
}
