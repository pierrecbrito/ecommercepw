package br.edu.ufrn.tads.ecommercepw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {}