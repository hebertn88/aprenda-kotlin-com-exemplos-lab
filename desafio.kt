class EmptyCollectionException(message: String): Throwable(message)

enum class Nivel { BASICO, INTERMEDIARIO, AVANCADO }

data class Usuario(val nome: String, val conteudosEstudados: MutableSet<ConteudoEducacional> = mutableSetOf()) {
    override fun toString(): String = "Usuario(nome=$nome)"   
}

// uso de infix function
infix fun Usuario.estudou(conteudo: ConteudoEducacional) = conteudosEstudados.add(conteudo)   

data class ConteudoEducacional(val nome: String, val nivel: Nivel, val duracao: Int)

data class Formacao(val nome: String, val conteudos: MutableSet<ConteudoEducacional> = mutableSetOf()) {

    val inscritos = mutableListOf<Usuario>()

    // TODO("Utilize o parâmetro $usuario para simular uma matrícula (usar a lista de $inscritos).")
    fun matricular(vararg usuarios: Usuario) = inscritos.addAll(usuarios)
        
    fun addConteudos(vararg conteudosArg: ConteudoEducacional) = conteudos.addAll(conteudosArg)
    
    fun progressoPorUsuario(usuario: Usuario): Number {
        // verifica se aluno foi matriculado
        if (!inscritos.contains(usuario)) throw IllegalArgumentException("Usuário não inscrito na Formação.")
        // verifica se ha cursos vinculados a formacao
        if (conteudos.isEmpty()) throw EmptyCollectionException("Não há Conteúdos Educacionais vinculados a Formação.")
        
        val totalConteudos = conteudos.size.toDouble()
        val totalConteudosEstudados = conteudos.intersect(usuario.conteudosEstudados).size.toDouble()
        return totalConteudosEstudados / totalConteudos * 100
    }

    // calcula duracao da Formacao a partir dos conteudos educacionais adicionados
    val duracao: Int
    get() {
        // verifica se ha cursos vinculados a formacao
        if (conteudos.isEmpty()) throw EmptyCollectionException("Não há Conteúdos Educacionais vinculados a Formação.")
        
        return conteudos.fold(0) { sum, c -> sum + c.duracao }
    }
    
    // calcula nivel da Formacao a partir dos conteudos educacionais adicionados
    val nivel: Nivel 
        get() {
            // verifica se ha cursos vinculados a formacao
            if (conteudos.isEmpty()) throw EmptyCollectionException("Não há Conteúdos Educacionais vinculados a Formação.")
         	
            return conteudos.groupingBy {it.nivel}
                .eachCount()
                .entries
                .sortedByDescending {it.value}
                .first().key
        }
}

fun main() {
    // TODO("Analise as classes modeladas para este domínio de aplicação e pense em formas de evoluí-las.")
    // TODO("Simule alguns cenários de teste. Para isso, crie alguns objetos usando as classes em questão.")
    val aprendendoKotlin = Formacao("Aprendendo Kotlin na Prática em Sua Documentação Oficial")
    
    // cria instancias de conteudos educacionais
    val c01Conhecendo = ConteudoEducacional("Conhecendo o Kotlin e Sua Documentação Oficial", Nivel.BASICO, 60)
    val c02Introducao = ConteudoEducacional("Introdução Prática à Linguagem de Programação Kotlin", Nivel.BASICO, 120)
    val c03ControleFluxo = ConteudoEducacional("Estruturas de Controle de Fluxo e Coleções em Kotlin", Nivel.BASICO, 120)
    val c04POO = ConteudoEducacional("Orientação a Objetos e Tipos de Classes na Prática com Kotlin", Nivel.BASICO, 120)
    val c05Funcoes = ConteudoEducacional("O Poder das Funções em Kotlin", Nivel.BASICO, 120)
    val c06Excecoes = ConteudoEducacional("Tratamento de Exceções em Kotlin", Nivel.INTERMEDIARIO, 120)
    
    // adiciona conteudos educacionais a formacao
    aprendendoKotlin.addConteudos(c01Conhecendo, c02Introducao, c03ControleFluxo, c04POO, c05Funcoes, c06Excecoes)
    
    // cria instancias de usuario
    val u1 = Usuario("Hebert")
    val u2 = Usuario("Maria")
    
    aprendendoKotlin.matricular(u1, u2) // matricula usuario na formacao
    
    println(aprendendoKotlin.inscritos) // exibe usuarios inscritos na formacao
    println(aprendendoKotlin.conteudos) // exibe conteudos da formacao
    
    println()
    
    u1 estudou c01Conhecendo // usando infix function
    
    println("${u1.nome} estudou ${u1.conteudosEstudados}")

    println(String.format("Progresso de %s em %s: %.2f%%", u1.nome, aprendendoKotlin.nome, aprendendoKotlin.progressoPorUsuario(u1)))
    
    val u3 = Usuario("Joao")
    aprendendoKotlin.matricular(u3)
    
    println(String.format("Progresso de %s em %s: %.2f%%", u3.nome, aprendendoKotlin.nome, aprendendoKotlin.progressoPorUsuario(u3)))
    
    val f2 = Formacao("f2")
    
    // lança exceção IllegalArgumentException: usuario nao matriculado na formacao
    // println(f2.progressoPorUsuario(u3))
    
    f2.matricular(u3)
    
    // lança exceção EmptyCollectionException: formacao sem conteudos vinculados
    // println(f2.progressoPorUsuario(u3))
}