package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.CategoriaRepository;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.CategoriaInexistenteException;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Lancamento salvar(Lancamento lancamento) {
		validaPessoa(lancamento);
		validaCategoria(lancamento);
		
		return lancamentoRepository.save(lancamento);
	}
	
	public Lancamento atualizarLancamento(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamento(codigo);
		validaPessoa(lancamento);	
		validaCategoria(lancamento);
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		lancamentoRepository.save(lancamentoSalvo);
		return lancamentoSalvo;
	}
	
	private Lancamento buscarLancamento(Long codigo){
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return lancamentoSalvo;
	}
	
	private void validaPessoa(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if (pessoa == null || pessoa.getAtivo() == false) {
			throw new PessoaInexistenteOuInativaException();
		}
	}
	
	private void validaCategoria(Lancamento lancamento) {
		Categoria categoria = categoriaRepository.findOne(lancamento.getCategoria().getCodigo());
		
		if (categoria == null) {
			throw new CategoriaInexistenteException();
		}
	}
}
