package bean;

import java.util.List;

/**
 * Created by admin on 2016/3/18.
 */
public class HonorMedals {
    private int status;
    private List<MedalData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MedalData> getDatas() {
        return data;
    }

    public void setDatas(List<MedalData> datas) {
        this.data = datas;
    }

   public class MedalData{
        private String typ;
        private String typnm;
        private String nm;
        private String periodtyp;
        private int count;

        public String getTyp() {
            return typ;
        }

        public void setTyp(String typ) {
            this.typ = typ;
        }

        public String getNm() {
            return nm;
        }

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getPeriodtyp() {
            return periodtyp;
        }

        public void setPeriodtyp(String periodtyp) {
            this.periodtyp = periodtyp;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
       public String getTypnm() {
           return typnm;
       }

       public void setTypnm(String typnm) {
           this.typnm = typnm;
       }
    }
}
