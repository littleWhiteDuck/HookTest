package me.duck.hooktest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.UUID;

import me.duck.hooktest.bean.UseBean;
import me.duck.hooktest.databinding.ActivityMainBinding;
import me.duck.hooktest.view.PopupView;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MainActivity extends AppCompatActivity {

    private static final String config = "{\"packageName\":\"me.duck.hooktest\",\"appName\":\"HookTest\",\"versionName\":\"1.4\",\"description\":\"\",\"configs\":\"[{\\\"mode\\\":1,\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"alterParams\\\",\\\"params\\\":\\\"I,Z,java.lang.String\\\",\\\"resultValues\\\":\\\"10,,参数\\\"},{\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"getReturnValue\\\",\\\"resultValues\\\":\\\"测更更更试了返t回\\\"},{\\\"mode\\\":2,\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"breakPerform\\\"},{\\\"mode\\\":1,\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"getPrimitiveString\\\",\\\"params\\\":\\\"B,C,S,I,J,F,D,Z\\\",\\\"resultValues\\\":\\\"2b,2c,2short,2,2L,2f,2d,false\\\"},{\\\"mode\\\":3,\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"changeStaticFields\\\",\\\"fieldName\\\":\\\"isGood\\\",\\\"fieldClassName\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"resultValues\\\":\\\"true\\\"},{\\\"mode\\\":4,\\\"className\\\":\\\"me.duck.hooktest.bean.UseBean\\\",\\\"methodName\\\":\\\"<init>\\\",\\\"params\\\":\\\"Z,I\\\",\\\"fieldName\\\":\\\"isHook\\\",\\\"resultValues\\\":\\\"true\\\"},{\\\"mode\\\":4,\\\"className\\\":\\\"me.duck.hooktest.bean.UseBean\\\",\\\"methodName\\\":\\\"<init>\\\",\\\"params\\\":\\\"Z,I\\\",\\\"fieldName\\\":\\\"level\\\",\\\"resultValues\\\":\\\"188888\\\"},{\\\"mode\\\":3,\\\"className\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"methodName\\\":\\\"changeStaticFields\\\",\\\"fieldName\\\":\\\"scores\\\",\\\"fieldClassName\\\":\\\"me.duck.hooktest.MainActivity\\\",\\\"resultValues\\\":\\\"10000\\\"}]\",\"id\":170}\n";
    private static int scores = 0;
    private static boolean isGood = false;
    private UseBean useBean;
    private String cachePath;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MaterialToolbar materialToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(materialToolbar);
        new WindowPreferencesManager().applyEdgeToEdgePreference(getWindow());
        useBean = new UseBean(false, 0);
        changeStaticFields();
        initView();
        String path = getExternalFilesDir(null).getPath();
        cachePath = path + "/cache";
        new Thread(this::deleteCache).start();
    }

    private void deleteCache() {
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {
            File[] files = cacheFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            cacheFile.mkdir();
        }
    }

    private void changeStaticFields() {
        scores = 0;
        isGood = false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(languageUtil.attachBaseContext(newBase));
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        binding.tvReturnTest.setText(getString(R.string.main_test_return, getReturnValue()));
        binding.tvBreakTest.setText(getString(R.string.main_test_break, getString(R.string.main_break_text)));
        breakPerform();
        alterParams(0, false, "");
        binding.tvInstanceField.setText(getString(R.string.main_test_instance_field, useBean.getLevel(), useBean.isHook()));
        binding.tvStaticField.setText(getString(R.string.main_test_static_field, scores, isGood));
        byte byteTest = 1;
        short shortTest = 1;
        binding.tvPrimitiveType.setText(getString(R.string.main_test_primitive_type) + getPrimitiveString(byteTest, 'A', shortTest, 1, 1L, 1f, 1d, true));
        binding.tvBoxedPrimitiveType.setText(getString(R.string.main_test_boxed_primitive_type) + getBoxedPrimitiveString(Byte.valueOf("1"), 'A', Short.valueOf("1"),
                Integer.valueOf("1"), Long.valueOf("1"), Float.valueOf("1"), Double.valueOf("1"), Boolean.valueOf("true")));
        binding.btShowToast.setOnClickListener(view -> Toast.makeText(this, R.string.main_toast_tip, Toast.LENGTH_SHORT).show());
        binding.btShowDialog.setOnClickListener(view -> showDialog());
        binding.btShowSuperDialog.setOnClickListener(view -> showSuperDialog());
        binding.btPopup1.setOnClickListener(view -> showPopupWindow(view, false));
        binding.btPopup2.setOnClickListener(view -> showPopupWindow(view, true));
        binding.btReadClipboard.setOnClickListener(view -> readClipboard());
        binding.btWriteClipboard.setOnClickListener(View -> showPutClipDialog());
        binding.btReadAssets.setOnClickListener(v -> readAssetsText());
        binding.btCreateFile.setOnClickListener(v -> createFile());
        binding.btDeleteFile.setOnClickListener(v -> deleteFile());
        binding.btWriteFile.setOnClickListener(v -> writeText());
        binding.btReadFile.setOnClickListener(v -> readText());
    }

    private void deleteFile() {
        File cacheFile = new File(cachePath + "/" + UUID.randomUUID().toString());
        // fake delete
        cacheFile.delete();
        showMessageDialog("Success", cacheFile.getPath());
    }

    private void createFile() {
        String title = "Failure";
        File cacheFile = new File(cachePath + "/" + UUID.randomUUID().toString());
        try {
            boolean ok = cacheFile.createNewFile();
            if (ok) {
                title = "Success";
            }
        } catch (IOException ignored) {
        }
        showMessageDialog(title, cacheFile.getPath());
    }

    @SuppressWarnings("IOStreamConstructor")
    private void readText() {
        String fileName = UUID.randomUUID().toString();
        File cacheFile = new File(cachePath + "/" + fileName);
        String message;
        String title = "Success";
        try {
            cacheFile.createNewFile();
            writeText(cacheFile);
            message = readTextFromStream(new FileInputStream(cacheFile));
        } catch (IOException e) {
            message = "null";
            title = "Failure";
        }
        showMessageDialog(title, message + "\n" + cacheFile.getPath());
    }

    private void writeText() {
        String fileName = UUID.randomUUID().toString();
        File cacheFile = new File(cachePath + "/" + fileName);
        String title = "Failure";
        try {
            cacheFile.createNewFile();
            if (writeText(cacheFile)) {
                title = "Success";
            }
        } catch (IOException ignored) {
        }
        showMessageDialog(title, cacheFile.getPath());
    }

    private void readAssetsText() {
        String result;
        try {
            result = readTextFromStream(getAssets().open("test"));
        } catch (IOException e) {
            result = "error";
        }
        showMessageDialog("读取Assets文件", result);
    }

    private void showPutClipDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_dialog_title_input_text_for_clip)
                .setView(R.layout.layout_input)
                .setPositiveButton(R.string.main_dialog_confirm, (dialog, which) -> {
                    EditText editText = ((AlertDialog) dialog).findViewById(R.id.text_input_edit);
                    //noinspection ConstantConditions
                    toClip(editText.getText().toString().trim());
                })
                .setNegativeButton(getString(R.string.main_dialog_cancel), null)
                .show();
    }


    private void readClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        String title = "Failure";
        StringBuilder message = new StringBuilder();
        if (clipData != null) {
            title = "Success";
            for (int i = 0; i < clipData.getItemCount(); i++) {
                CharSequence text = clipData.getItemAt(i).getText();
                if (text == null) {
                    Intent pasteIntent = clipboard.getPrimaryClip().getItemAt(i).getIntent();
                    if (pasteIntent != null) {
                        message.append(pasteIntent);
                    } else {
                        message.append(clipData.getItemAt(i).getUri());
                    }
                } else {
                    message.append(text);
                }
                if (i != clipData.getItemCount()) {
                    message.append("\n");
                }
            }
        } else {
            message.append("null");
        }
        showMessageDialog(title, message.toString());
    }

    private void showMessageDialog(String title, String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.main_dialog_confirm), null)
                .setNegativeButton(getString(R.string.main_dialog_cancel), null)
                .show();
    }

    @Keep
    @SuppressWarnings("SameParameterValue")
    private String getBoxedPrimitiveString(Byte valueOf, Character a, Short valueOf1, Integer valueOf2, Long valueOf3, Float valueOf4, Double valueOf5, Boolean aTrue) {
        return "\nByte(1) -> " + valueOf + "\nCharacter(A) -> " + a + "\nShort(1) -> " + valueOf1 + "\nInteger(1) -> " + valueOf2 + "\nLong(1) -> " + valueOf3 + "\nFloat(1) -> " + valueOf4 + "\nDouble(1) -> " + valueOf5 + "\nBoolean(true) ->" + aTrue;
    }

    @Keep
    @SuppressWarnings("SameParameterValue")
    private String getPrimitiveString(byte byteTest, char a, short shortTest, int i, long l, float v, double v1, boolean b) {
        return "\nbyte(1) -> " + byteTest + "\nchar(A) -> " + a + "\nshort(1) -> " + shortTest + "\nint(1) -> " + i + "\nlong(1) -> " + l + "\nfloat(1) -> " + v + "\ndouble(1) -> " + v1 + "\nboolean(true) -> " + b;
    }


    @SuppressLint({"Range", "UseCompatLoadingForDrawables"})
    private void showPopupWindow(View view, boolean is2) {
        PopupView popupView = new PopupView(this);
        popupView.setBackground(getDrawable(R.drawable.popup_window_background));
        popupView.title.setText(R.string.main_popup_title);
        popupView.content.setText(getString(R.string.main_popup_content));
        popupView.confirm.setText(R.string.main_popup_confirm);
        popupView.cancel.setText(R.string.main_popup_cancel);
        popupView.measure(View.MeasureSpec.makeMeasureSpec((int) (getWindowWidth() * 0.8), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.AT_MOST));
        PopupWindow popupWindow = new PopupWindow(popupView, popupView.getMeasuredWidth(), popupView.getMeasuredHeight(), true);
        if (is2) {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        } else {
            popupWindow.showAsDropDown(view);
        }
    }

    private int getWindowWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void showDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_dialog_title)
                .setMessage(R.string.main_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.main_dialog_confirm, null)
                .setNegativeButton(R.string.main_dialog_cancel, null)
                .show();
    }

    private void showSuperDialog() {
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_super_dialog_title)
                .setMessage(R.string.main_super_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.main_super_dialog_confirm, (dialogInterface, i) -> reflectDialogDismiss((AlertDialog) dialogInterface, false))
                .setNegativeButton(R.string.main_super_dialog_cancel, (dialogInterface, i) -> reflectDialogDismiss((AlertDialog) dialogInterface, false))
                .setNeutralButton(R.string.main_super_dialog_cancel_plus, (dialogInterface, i) -> reflectDialogDismiss((AlertDialog) dialogInterface, true)).create();
        alertDialog.show();
        reflectDialogDismiss(alertDialog, false);
    }

    private void reflectDialogDismiss(AlertDialog dialog, boolean cancelAble) {
        try {
            Field mShowing = Class.forName(Dialog.class.getName()).getDeclaredField("mShowing");
            mShowing.setAccessible(true);
            mShowing.set(dialog, cancelAble);
        } catch (Exception ignored) {

        }
    }

    private void alterParams(int i, boolean b, String s) {
        MaterialTextView tv_paramsTest = findViewById(R.id.tv_paramsTest);
        tv_paramsTest.setText(getString(R.string.main_test_params_hook, i, b, s));
    }

    private void breakPerform() {
        binding.tvBreakTest.setText(getString(R.string.main_test_break, getString(R.string.main_not_break_text)));
    }

    private String getReturnValue() {
        return getString(R.string.main_default_return_value);
    }

    @SuppressLint("WrongConstant")
    private void toClip(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("label", text));
        Toast.makeText(this, R.string.main_copy_config_tip, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_copy) {
            toClip(config);
        }
        return true;
    }

    private String readTextFromStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    private boolean writeText(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("Test Test Test");
            bufferedWriter.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}