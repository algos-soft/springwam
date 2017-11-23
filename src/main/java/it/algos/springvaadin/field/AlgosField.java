package it.algos.springvaadin.field;

import com.vaadin.ui.Component;
import it.algos.springvaadin.event.AEvent;
import it.algos.springvaadin.entity.AEntity;
import it.algos.springvaadin.presenter.AlgosPresenterImpl;
import org.springframework.context.ApplicationListener;

public interface AlgosField {


    public void doValue(AEntity entityBean);

    public void setName(String name);

    public String getName();

    public void setDescription(String description);

    public void saveSon();

    public AlgosPresenterImpl getFormPresenter();

    public void setSource(ApplicationListener<AEvent> formSource);

    public Component initContent() ;
}// end of interface
