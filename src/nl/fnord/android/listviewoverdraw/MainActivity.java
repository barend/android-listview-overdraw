package nl.fnord.android.listviewoverdraw;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    private static final String[] DATA = { "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot" };
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        adapter = new ArrayAdapter<String>(this, R.layout.activity_main__list_item, android.R.id.text1, new ArrayList<String>());
        ListView lv = (ListView) findViewById(android.R.id.list);

        View header = new View(this);
        header.setBackgroundColor(0xFFFFFFFF);
        header.setMinimumHeight(160);
        lv.addHeaderView(header);

        View footer = new View(this);
        footer.setMinimumHeight(160);
        footer.setBackgroundColor(0xFFFFFFFF);
        lv.addFooterView(footer);

        lv.setAdapter(adapter);
    }

    public void addButtonClicked(View v) {
        adapter.add(DATA[new Random().nextInt(DATA.length)]);
    }

    public void removeButtonClicked(View v) {
        if (adapter.getCount() > 0) {
            adapter.remove(adapter.getItem(0));
        }
    }

    public void growButtonClicked(View v) {
        View buttonbar = findViewById(R.id.main_view__buttonbar);
        buttonbar.getLayoutParams().height += 8;
        buttonbar.requestLayout();
    }

    public void shrinkButtonClicked(View v) {
        View buttonbar = findViewById(R.id.main_view__buttonbar);
        buttonbar.getLayoutParams().height -= 8;
        buttonbar.requestLayout();
    }
}
