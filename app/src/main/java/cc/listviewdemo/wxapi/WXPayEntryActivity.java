package cc.listviewdemo.wxapi;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cc.listviewdemo.R;
import cc.listviewdemo.activity.ConfirmOrderActivity;
import cc.listviewdemo.activity.OrderDetailActivity;
import cc.listviewdemo.activity.SHDetailsActivity;
import cc.listviewdemo.config.Config;
import cc.listviewdemo.config.SaveKey;
import cc.listviewdemo.view.Utils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Config.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode){
				case 0:
					Toast.makeText(WXPayEntryActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(WXPayEntryActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
					break;
			}
			if(ConfirmOrderActivity.mInstance!=null){
				ConfirmOrderActivity.mInstance.finish();
			}
			if(SHDetailsActivity.mInstance!=null){
				SHDetailsActivity.mInstance.finish();
			}
			WXPayEntryActivity.this.finish();
			if(OrderDetailActivity.mInstance!=null){
				OrderDetailActivity.closeThis();
			}
		}
	}
}