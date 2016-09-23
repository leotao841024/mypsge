package bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2016/3/21.
 */
public class GsonFormatDemo {


    /**
     * status : 0
     * data : [{"typ":"teamteam","typnm":"团队勋章","nm":"团队霸主日勋章","periodtyp":"teamteam_day","count":1},{"typ":"gallop","typnm":"飞奔勋章","nm":"给力日勋章","periodtyp":"gallop_day","count":0},{"typ":"awesome","typnm":"给力勋章","nm":"健步如飞日勋章","periodtyp":"awesome_day","count":0},{"typ":"personteam","typnm":"金质勋章","nm":"金质最棒日勋章","periodtyp":"personteam_day","count":0},{"typ":"teamteam","typnm":"团队勋章","nm":"团队霸主周勋章","periodtyp":"teamteam_week","count":0},{"typ":"gallop","typnm":"飞奔勋章","nm":"给力周勋章","periodtyp":"gallop_week","count":0},{"typ":"awesome","typnm":"给力勋章","nm":"健步如飞周勋章","periodtyp":"awesome_week","count":0},{"typ":"personteam","typnm":"金质勋章","nm":"金质最棒周勋章","periodtyp":"personteam_week","count":0},{"typ":"teamteam","typnm":"团队勋章","nm":"团队霸主月勋章","periodtyp":"teamteam_month","count":0},{"typ":"gallop","typnm":"飞奔勋章","nm":"给力月勋章","periodtyp":"gallop_month","count":0},{"typ":"awesome","typnm":"给力勋章","nm":"健步如飞月勋章","periodtyp":"awesome_month","count":0},{"typ":"personteam","typnm":"金质勋章","nm":"金质最棒月勋章","periodtyp":"personteam_month","count":0}]
     */

    private int status;
    /**
     * typ : teamteam
     * typnm : 团队勋章
     * nm : 团队霸主日勋章
     * periodtyp : teamteam_day
     * count : 1
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("typ")
        private String type;
        @SerializedName("typnm")
        private String typenm;
        @SerializedName("nm")
        private String name;
        @SerializedName("periodtyp")
        private String periodtype;
        private int count;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypenm() {
            return typenm;
        }

        public void setTypenm(String typenm) {
            this.typenm = typenm;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPeriodtype() {
            return periodtype;
        }

        public void setPeriodtype(String periodtype) {
            this.periodtype = periodtype;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
