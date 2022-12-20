package com.example.fyp_mobile.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fyp_mobile.R
import com.example.fyp_mobile.databinding.FragmentEventBinding
import com.example.fyp_mobile.ui.activities.UserProfActivity

class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val notificationsViewModel =
        //ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textEvent

        textView.text = "This is the event fragment"

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){

            R.id.action_profile ->{
                startActivity(Intent(activity, UserProfActivity::class.java))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}