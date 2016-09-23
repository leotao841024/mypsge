package manager;

import java.util.List;

import bean.MinuteStep;
import bean.UploadDataBase;

public abstract class UploadBase {
	public abstract boolean uploadBase(UploadDataBase base);
	public abstract boolean uploadDetail(List<MinuteStep> details);
}