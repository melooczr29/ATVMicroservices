package com.springfield.springfield_rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory(name = "solicitacaoStateMachineFactory")
public class ConfiguracaoMaquinaEstados extends StateMachineConfigurerAdapter<EstadoSolicitacao, EventoSolicitacao> {

    @Override
    public void configure(StateMachineStateConfigurer<EstadoSolicitacao, EventoSolicitacao> states) throws Exception {
        states
                .withStates()
                .initial(EstadoSolicitacao.SOLICITADO)
                .states(EnumSet.allOf(EstadoSolicitacao.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EstadoSolicitacao, EventoSolicitacao> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(EstadoSolicitacao.SOLICITADO).target(EstadoSolicitacao.AGUARDANDO_ANALISE)
                .event(EventoSolicitacao.ANALISAR)
                .and()
                .withExternal()
                .source(EstadoSolicitacao.AGUARDANDO_ANALISE).target(EstadoSolicitacao.CONCLUIDO)
                .event(EventoSolicitacao.CONCLUIR);
    }
}