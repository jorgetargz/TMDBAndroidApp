package org.jorgetargz.movies.framework.details_person

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jorgetargz.movies.R
import org.jorgetargz.movies.databinding.FragmentDetailsPersonBinding
import org.jorgetargz.movies.framework.common.Constantes
import org.jorgetargz.movies.framework.main.MainActivity
import org.jorgetargz.movies.framework.utils.loadUrlFromTMDBW342Size


@AndroidEntryPoint
class DetailsPersonFragment : Fragment() {

    private lateinit var binding: FragmentDetailsPersonBinding
    private val viewModel: DetailsPersonViewModel by viewModels()

    private val argIdPerson = Constantes.ID_PERSON
    private var idPerson: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPerson = it.getInt(argIdPerson)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setupBinding()

        viewModel.handleEvent(DetailsPersonContract.DetailsPersonEvent.LoadPerson(idPerson))

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { value ->
                    value.person.let { person ->
                        with(binding) {
                            person.profilePath?.let { ivProfile.loadUrlFromTMDBW342Size(it) }
                            tvValueName.text = person.name
                            tvValueJob.text = person.knownForDepartment
                            tvValueBiography.text = person.biography
                        }
                    }
                    value.error?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    value.isLoading.let {
                        ((activity as MainActivity).findViewById(R.id.loadingAnimation) as ProgressBar)
                            .visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
            }
        }

        return binding.root
    }

    private fun setupBinding() {
        binding = FragmentDetailsPersonBinding.inflate(layoutInflater)
    }
}