package bash.sharon.photosApp.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bash.sharon.photosApp.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()
    lateinit var binding : FragmentOverviewBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        //val binding = GridViewItemBinding.inflate(inflater,container,false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel
        binding.photosGrid.adapter = PhotoGridAdapter()
        binding.plainTextInput.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String?): Boolean {
                    viewModel.search(text)
                    return true
                }

                override fun onQueryTextChange(text: String?): Boolean {
                    if (text.isNullOrEmpty()) viewModel.search(text)
                    return false
                }

            })
        }
        viewModel._status.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        return binding.root
    }


}