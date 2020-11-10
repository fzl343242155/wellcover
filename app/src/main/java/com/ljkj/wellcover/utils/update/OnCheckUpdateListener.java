package com.ljkj.wellcover.utils.update;

public interface OnCheckUpdateListener {
    /**
     * 发现新版本
     *
     * @param versionName       新版Apk版本名称
     * @param newVersionContent 新版Apk更新内容
     * @param apkUrl            新版ApkUrl
     */
    void onFindNewVersion(String versionName, String newVersionContent, String apkUrl);

    /**
     * 当前版本已是最新版本
     */
    void onNewest();
}
