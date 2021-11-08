package tardova.online.copybook

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tardova.online.copybook.model.Document
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.parcel.Parcelize
import tardova.online.copybook.model.IdTheme

class DocumentAdapter(var dataSet: List<IdTheme>, private val onClick: (Long) -> Unit ) :
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(document: IdTheme) {
            view.listItemTheme.text = document.theme
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)

        val vi = ViewHolder(view)
        vi.view.setOnClickListener{
            onClick(dataSet[vi.adapterPosition].uid)
        }
        return vi
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}

@Parcelize // сущность превращаем в набор битов для сохраниения в файл
class DocumentListHolder(val documents: List<Document>) : Parcelable