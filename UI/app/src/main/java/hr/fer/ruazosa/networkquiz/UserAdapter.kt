package hr.fer.ruazosa.networkquiz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.fer.ruazosa.networkquiz.entity.SelectableUser

class UserAdapter (val opponents: MutableList<SelectableUser>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var data : MutableList<SelectableUser>? = opponents

    private val TYPE_INACTIVE=0
    private val TYPE_ACTIVE=1


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView : TextView = view.findViewById(R.id.usernameTextView)
        val addOpponentButton: ImageButton = view.findViewById(R.id.addImageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout : Int? =  null
        if (viewType == TYPE_INACTIVE ){ layout = R.layout.player_item }
        else { layout = R.layout.player_item_selected }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent , false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.usernameTextView.text = data!![position].user.username
        holder.addOpponentButton.setOnClickListener {
            data!![position].selected = !data!![position].selected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun getItemViewType(position: Int): Int {
        if (data?.get(position)?.selected!!){
            return TYPE_ACTIVE
        } else
            return TYPE_INACTIVE
    }

    fun filterPlayers(filteredList : MutableList<SelectableUser>){
        data = filteredList
        notifyDataSetChanged()
    }
}